package com.vaultionizer.vaultserver.controllers;


import com.vaultionizer.vaultserver.model.dto.FileUploadDto;
import com.vaultionizer.vaultserver.model.dto.GenericAuthDto;
import com.vaultionizer.vaultserver.model.dto.GetSpacesResponseDto;
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

    @Autowired
    public FileController(SessionService sessionService, SpaceService spaceService, UserAccessService userAccessService,
                          RefFileService refFileService, PendingUploadService pendingUploadService) {
        this.sessionService = sessionService;
        this.spaceService = spaceService;
        this.userAccessService = userAccessService;
        this.refFileService = refFileService;
        this.pendingUploadService = pendingUploadService;
    }


    @RequestMapping(value = "/api/file/upload", method = RequestMethod.POST)
    @ApiOperation(value = "Requests to upload a variable amount of files.",
            response = Long.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "The response contains the starting id the server will save the files as."),
            @ApiResponse(code = 400, message = "SpaceID is invalid (< 0) or amount of files to be uploaded is invalid (<= 0)."),
            @ApiResponse(code = 401, message = "The user either does not exist or the key is wrong. User is thus not authorized."),
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
}
