package com.vaultionizer.vaultserver.controllers;

import com.vaultionizer.vaultserver.model.db.SpaceModel;
import com.vaultionizer.vaultserver.model.dto.*;
import com.vaultionizer.vaultserver.resource.SpaceRepository;
import com.vaultionizer.vaultserver.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "/api/spaces/", description = "Controller that manages spaces.")
@RestController
public class SpaceController {
    private final SpaceRepository spaceRepository;
    private final SessionService sessionService;
    private final SpaceService spaceService;
    private final RefFileService refFileService;
    private final PendingUploadService pendingUploadService;
    private final FileService fileService;
    private final UserAccessService userAccessService;

    @Autowired
    public SpaceController(SpaceRepository spaceRepository, SessionService sessionService, SpaceService spaceService,
                           RefFileService refFileService, PendingUploadService pendingUploadService, FileService fileService,
                           UserAccessService userAccessService) {
        this.spaceRepository = spaceRepository;
        this.sessionService = sessionService;
        this.spaceService = spaceService;
        this.refFileService = refFileService;
        this.pendingUploadService = pendingUploadService;
        this.fileService = fileService;
        this.userAccessService = userAccessService;
    }



    @RequestMapping(value = "/api/spaces/getAll", method = RequestMethod.POST)
    @ApiOperation(value = "Returns all spaces a user has access to.",
        response = GetSpacesResponseDto.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The response contains all spaces the user has access to."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
    })
    @ResponseBody ResponseEntity<?>
    getAllSpaces(@RequestBody AuthWrapperDto req){
        GenericAuthDto auth = req.getAuth();
        if (!sessionService.getSession(auth.getUserID(), auth.getSessionKey())){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(
                spaceService.getSpacesAccessible(auth.getUserID()), HttpStatus.OK);
    }


    @RequestMapping(value = "/api/spaces/create", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new space.",
        response = Long.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The space was created successfully. The returned value is the space's ID."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong."),
    })
    @ResponseBody ResponseEntity<?>
    createSpace(@RequestBody CreateSpaceDto req){
        if (!sessionService.getSession(req.getAuth().getUserID(), req.getAuth().getSessionKey())){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        var refFile = refFileService.addNewRefFile(req.getReferenceFile());
        SpaceModel model = spaceRepository.save(new SpaceModel(req.getAuth().getUserID(), refFile.getRefFileId(),
                req.isPrivate(), req.getAuthKey()));
        userAccessService.addUserAccess(model.getSpaceID(), req.getAuth().getUserID());
        return new ResponseEntity<>(model.getSpaceID(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/spaces/join", method = RequestMethod.PUT)
    @ApiOperation(value = "Adds the user to the space.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user was successfully added to the space."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "Either the space with given ID does not exist, it is private or the authorization key is wrong.")
    })
    @ResponseBody ResponseEntity<?>
    joinSpace(@RequestBody JoinSpaceDto req){
        if (!sessionService.getSession(req.getAuth().getUserID(), req.getAuth().getSessionKey())){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        if (spaceService.checkSpaceCredentials(req.getSpaceID(), req.getAuthKey())){
            userAccessService.addUserAccess(req.getSpaceID(), req.getAuth().getUserID());
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/api/spaces/key", method = RequestMethod.POST)
    @ApiOperation(  value = "Returns the authentication key of a file.",
                    response = SpaceAuthKeyResponseDto.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user was successfully added to the space."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "Either the space with given ID does not exist, it is private or the authorization key is wrong.")
    })
    @ResponseBody ResponseEntity<?>
    getAuthKey(@RequestBody SpaceAuthKeyDto req){
        if (!sessionService.getSession(req.getAuth().getUserID(), req.getAuth().getSessionKey())){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        if (spaceService.checkDeleted(req.getSpaceID()) &&
            userAccessService.userHasAccess(req.getAuth().getUserID(), req.getSpaceID())){
            return new ResponseEntity<>(spaceRepository.getSpaceAuthKey(req.getSpaceID()), HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }


    @RequestMapping(value = "/api/spaces/delete/{spaceID}", method = RequestMethod.DELETE)
    @ApiOperation(  value = "Deletes the specified space if permitted.",
            response = SpaceAuthKeyResponseDto.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user was successfully added to the space."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "Either the space with given ID does not exist, it is private or the authorization key is wrong."),
            @ApiResponse(code = 412, message = "Space is probably currently in deletion process.")
    })
    @ResponseBody ResponseEntity<?>
    deleteSpace(@RequestBody AuthWrapperDto req, @PathVariable Long spaceID){
        GenericAuthDto auth = req.getAuth();
        if (!sessionService.getSession(auth.getUserID(), auth.getSessionKey())){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        if (!userAccessService.userHasAccess(auth.getUserID(), spaceID) ||
                !spaceService.checkCreator(spaceID, auth.getUserID())){
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        if (!spaceService.markSpaceDeleted(spaceID)){
            return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
        }
        deleteSpaceRoutine(spaceID);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    public void deleteSpaceRoutine(Long spaceID){
        userAccessService.deleteAllWithSpace(spaceID);

        fileService.deleteAllFilesInSpace(spaceID);

        pendingUploadService.deleteAllPendingUploads(spaceID);

        Long refFileID = spaceService.getRefFileID(spaceID);
        refFileService.deleteRefFile(refFileID);
        spaceService.deleteSpace(spaceID);
    }
}
