package com.vaultionizer.vaultserver.controllers;


import com.vaultionizer.vaultserver.model.dto.ReadRefFileDto;
import com.vaultionizer.vaultserver.model.dto.UpdateRefFileDto;
import com.vaultionizer.vaultserver.service.RefFileService;
import com.vaultionizer.vaultserver.service.SessionService;
import com.vaultionizer.vaultserver.service.SpaceService;
import com.vaultionizer.vaultserver.service.UserAccessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "/api/refFile/", description = "Controller that handles the ref file CRUD requests.")
@RestController
public class RefFileController {
    private final SessionService sessionService;
    private final UserAccessService userAccessService;
    private final SpaceService spaceService;
    private final RefFileService refFileService;

    @Autowired
    public RefFileController(SessionService sessionService, UserAccessService userAccessService,
                             SpaceService spaceService, RefFileService refFileService) {
        this.sessionService = sessionService;
        this.userAccessService = userAccessService;
        this.spaceService = spaceService;
        this.refFileService = refFileService;
    }

    @RequestMapping(value = "/api/refFile/read", method = RequestMethod.POST)
    @ApiOperation(value = "Read the reference file of the specified space or if lastRead is older than last update on reference file, NOT_MODIFIED is sent as status.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The response contains the content of the current ref file."),
            @ApiResponse(code = 304, message = "The response is empty because the last time the reference file was fetched is the newest version."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "Either the space with given ID does not exist or the user has no access to that space."),
            @ApiResponse(code = 500, message = "Inconsistencies on the server side. Should never be the case.")
    })
    @ResponseBody ResponseEntity<?>
    readRefFile(@RequestBody ReadRefFileDto req){
        if (!sessionService.getSession(req.getAuth().getUserID(), req.getAuth().getSessionKey())){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        if (userAccessService.userHasAccess(req.getAuth().getUserID(), req.getSpaceID())){
            Long refFileID = spaceService.getRefFileID(req.getSpaceID());
            if (refFileID == -1L) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // if the last fetched version is latest, just tell user not modified
            if (req.getLastRead() != null && !refFileService.hasNewVersion(refFileID, req.getLastRead())){
                return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
            }
            String content = refFileService.readRefFile(refFileID);
            if (content == null){
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(content, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/api/refFile/update", method = RequestMethod.PUT)
    @ApiOperation(value = "Update the reference file of the specified space.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The response contains the content of the current ref file."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "Either the space with given ID does not exist or the user has no access to that space."),
            @ApiResponse(code = 500, message = "Inconsistencies on the server side. Should never be the case.")
    })
    @ResponseBody ResponseEntity<?>
    updateRefFile(@RequestBody UpdateRefFileDto req){
        if (!sessionService.getSession(req.getAuth().getUserID(), req.getAuth().getSessionKey())){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        if (userAccessService.userHasAccess(req.getAuth().getUserID(), req.getSpaceID())){
            Long refFileID = spaceService.getRefFileID(req.getSpaceID());
            if (refFileID == -1L) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            boolean success = refFileService.updateRefFile(refFileID, req.getContent());
            if (!success){
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }
}
