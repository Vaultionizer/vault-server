package com.vaultionizer.vaultserver.controllers;


import com.vaultionizer.vaultserver.helpers.AccessCheckerUtil;
import com.vaultionizer.vaultserver.helpers.FileStatus;
import com.vaultionizer.vaultserver.model.dto.FileUploadDto;
import com.vaultionizer.vaultserver.model.dto.GenericAuthDto;
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
    private final AccessCheckerUtil accessCheckerUtil;


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
        accessCheckerUtil = new AccessCheckerUtil(sessionService, userAccessService, spaceService);
    }

    @PostMapping(value = "/api/file/{spaceID}/upload")
    @ApiOperation(value = "Requests to upload a variable amount of files.",
            response = Long.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "The response contains the starting id the server will save the files as."),
            @ApiResponse(code = 400, message = "SpaceID is invalid (< 0) or amount of files to be uploaded is invalid (<= 0)."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "The user has no rights for the requested space."),
            @ApiResponse(code = 406, message = "The user has no write access."),
            @ApiResponse(code = 404, message = "A consistency error occurred.")
    })
    public @ResponseBody
    ResponseEntity<?>
    uploadFiles(@RequestBody FileUploadDto req, @RequestHeader("xAuth") GenericAuthDto auth, @PathVariable("spaceID") Long spaceID) {
        Long sessionID = sessionService.getSessionID(auth.getUserID(), auth.getSessionKey());
        if (sessionID == -1) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        if (req.getAmountFiles() <= 0 || spaceID < 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (userAccessService.userHasAccess(auth.getUserID(), spaceID)) {
            if (!spaceService.userHasWriteAccess(spaceID, auth.getUserID())) {
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
            Long refFileID = spaceService.getRefFileID(spaceID);
            if (refFileID == -1) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            // retrieving the index the files will be saved as
            Long saveIndex = refFileService.requestUploadFiles(refFileID, (long) req.getAmountFiles());

            if (saveIndex == -1) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            // add files to pending upload table (with appropriate sessionID)
            pendingUploadService.addFilesToUpload(spaceID, sessionID, (long) req.getAmountFiles(), saveIndex);

            return new ResponseEntity<>(saveIndex, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }


    @PutMapping(value = "/api/file/{spaceID}/{saveIndex}/download")
    @ApiOperation(value = "Requests to download a specific file.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The file will be send via websocket to respective location (taking the websocketToken into account)."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "The user has no rights for the requested space."),
            @ApiResponse(code = 404, message = "The requested file does not exist."),
            @ApiResponse(code = 423, message = "The requested file is currently either being uploaded or modified. Thus, the file is locked."),
            @ApiResponse(code = 500, message = "A consistency error occurred. Should never be the case. Bug the developer!")
    })
    public @ResponseBody
    ResponseEntity<?>
    downloadFile(@RequestHeader("xAuth") GenericAuthDto auth, @PathVariable Long spaceID, @PathVariable Long saveIndex) {
        String websocketToken = sessionService.
                getSessionWebsocketToken(auth.getUserID(), auth.getSessionKey());
        if (websocketToken == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        var httpStatus = accessCheckerUtil.checkAccess(auth, spaceID);
        if (httpStatus != null) return new ResponseEntity<>(null, httpStatus);

        FileStatus status = fileService.setDownloadFile(spaceID, saveIndex);
        if (status == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        switch (status) {
            case READ_FROM:
                // read file and send to websocket endpoint
                Runnable runnable = () -> websocketController.download(websocketToken, spaceID, saveIndex);
                (new Thread(runnable)).start();
                return new ResponseEntity<>(null, HttpStatus.OK);
            case MODIFYING, UPLOADING:
                return new ResponseEntity<>(null, HttpStatus.LOCKED);
            default:
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/api/file/{spaceID}/{saveIndex}")
    @ApiOperation(value = "Requests to delete a specific file.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File has successfully been deleted."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "The user has no rights for the requested space."),
            @ApiResponse(code = 406, message = "The user has no write access."),
            @ApiResponse(code = 423, message = "The requested file is currently either being uploaded or modified. Thus, the file is locked."),
    })
    public @ResponseBody
    ResponseEntity<?>
    deleteFile(@RequestHeader("xAuth") GenericAuthDto auth, @PathVariable Long spaceID, @PathVariable Long saveIndex) {
        HttpStatus status = accessCheckerUtil.checkWriteAccess(auth, spaceID);
        if (status != null) return new ResponseEntity<>(null, status);

        boolean success = fileService.deleteFile(spaceID, saveIndex);
        if (!success) {
            return new ResponseEntity<>(null, HttpStatus.LOCKED);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @PutMapping(value = "/api/file/{spaceID}/{saveIndex}/update")
    @ApiOperation(value = "Requests to update a specific file.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File has successfully been marked for updating."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "The user has no rights for the requested space."),
            @ApiResponse(code = 406, message = "The user has no write access."),
            @ApiResponse(code = 409, message = "Some conflict occurred."),
    })
    public @ResponseBody
    ResponseEntity<?>
    updateFile(@RequestHeader("xAuth") GenericAuthDto auth, @PathVariable Long spaceID, @PathVariable Long saveIndex) {
        HttpStatus status = accessCheckerUtil.checkWriteAccess(auth, spaceID);
        if (status != null) return new ResponseEntity<>(null, status);

        boolean granted = pendingUploadService.updateFile(spaceID,
                sessionService.getSessionID(auth.getUserID(),
                        auth.getSessionKey()), saveIndex);

        if (!granted) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }
}
