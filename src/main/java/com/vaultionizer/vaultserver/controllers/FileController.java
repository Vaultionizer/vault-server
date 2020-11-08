package com.vaultionizer.vaultserver.controllers;


import com.vaultionizer.vaultserver.helpers.FileStatus;
import com.vaultionizer.vaultserver.model.dto.DeleteFileDto;
import com.vaultionizer.vaultserver.model.dto.FileDownloadDto;
import com.vaultionizer.vaultserver.model.dto.FileUploadDto;
import com.vaultionizer.vaultserver.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "/api/file/", description = "Controller that handles file crud requests.")
@RestController
public class FileController {
    private final SessionService sessionService;
    private final SpaceService spaceService;
    private final UserAccessService userAccessService;
    private final RefFileService refFileService;
    private final PendingUploadService pendingUploadService;
    private final FileService fileService;
    private final WebsocketController websocketController;


    @Autowired
    public FileController(SessionService sessionService, SpaceService spaceService,
                          UserAccessService userAccessService, RefFileService refFileService,
                          PendingUploadService pendingUploadService, FileService fileService,
                          WebsocketController websocketController) {
        this.sessionService = sessionService;
        this.spaceService = spaceService;
        this.userAccessService = userAccessService;
        this.refFileService = refFileService;
        this.pendingUploadService = pendingUploadService;
        this.fileService = fileService;
        this.websocketController = websocketController;
    }




    @RequestMapping(value = "/api/file/upload", method = RequestMethod.POST)
    @ApiOperation(value = "Requests to upload a variable amount of files.",
            response = Long.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "The response contains the starting id the server will save the files as."),
            @ApiResponse(code = 400, message = "SpaceID is invalid (< 0) or amount of files to be uploaded is invalid (<= 0)."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "The user has no rights for the requested space."),
            @ApiResponse(code = 404, message = "A consistency error occurred.")
    })
    @ResponseBody ResponseEntity<?>
    uploadFiles(@RequestBody FileUploadDto req){
        if (req.getAmountFiles() <= 0 || req.getSpaceID() < 0){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Long sessionID = sessionService.getSessionID(req.getAuth().getUserID(), req.getAuth().getSessionKey());
        if (sessionID == -1){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        if (userAccessService.userHasAccess(req.getAuth().getUserID(), req.getSpaceID())){
            Long refFileID = spaceService.getRefFileID(req.getSpaceID());
            if (refFileID == -1){
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            // retrieving the index the files will be saved as
            Long saveIndex = refFileService.requestUploadFiles(refFileID, (long) req.getAmountFiles());

            if (saveIndex == -1){
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            // add files to pending upload table (with appropriate sessionID)
            pendingUploadService.addFilesToUpload(req.getSpaceID(), sessionID, (long) req.getAmountFiles(), saveIndex);

            return new ResponseEntity<>(saveIndex, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }


    @RequestMapping(value = "/api/file/download", method = RequestMethod.PUT)
    @ApiOperation(value = "Requests to download a specific file.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The file will be send via websocket to respective location (taking the websocketToken into account)."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "The user has no rights for the requested space."),
            @ApiResponse(code = 404, message = "The requested file does not exist."),
            @ApiResponse(code = 423, message = "The requested file is currently either being uploaded or modified. Thus, the file is locked."),
            @ApiResponse(code = 500, message = "A consistency error occurred. Should never be the case. Bug the developer!")
    })
    @ResponseBody ResponseEntity<?>
    downloadFile(@RequestBody FileDownloadDto req){
        String websocketToken = sessionService.
                getSessionWebsocketToken(req.getAuth().getUserID(), req.getAuth().getSessionKey());
        if (websocketToken == null){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        if (!userAccessService.userHasAccess(req.getAuth().getUserID(), req.getSpaceID())){
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        FileStatus status = fileService.setDownloadFile(req.getSpaceID(), req.getSaveIndex());
        if (status == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        switch (status){
            case READ_FROM:
                // read file and send to websocket endpoint
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        websocketController.download(websocketToken, req.getSpaceID(), req.getSaveIndex());
                    }
                };
                (new Thread(runnable)).start();
                return new ResponseEntity<>(null, HttpStatus.OK);
            case MODIFYING:
            case UPLOADING:
                return new ResponseEntity<>(null, HttpStatus.LOCKED);
            default:
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/file/delete", method = RequestMethod.PUT)
    @ApiOperation(value = "Requests to delete a specific file.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File has successfully been deleted."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "The user has no rights for the requested space."),
            @ApiResponse(code = 423, message = "The requested file is currently either being uploaded or modified. Thus, the file is locked."),
    })
    @ResponseBody ResponseEntity<?>
    deleteFile(@RequestBody DeleteFileDto req){
        String websocketToken = sessionService.
                getSessionWebsocketToken(req.getAuth().getUserID(), req.getAuth().getSessionKey());
        if (!sessionService.getSession(req.getAuth().getUserID(), req.getAuth().getSessionKey())){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        if (!userAccessService.userHasAccess(req.getAuth().getUserID(), req.getSpaceID())){
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        boolean success = fileService.deleteFile(req.getSpaceID(), req.getSaveIndex());
        if (!success){
            return new ResponseEntity<>(null, HttpStatus.LOCKED);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
