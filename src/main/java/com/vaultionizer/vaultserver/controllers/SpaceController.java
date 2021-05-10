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
    private final SessionService sessionService;
    private final SpaceService spaceService;
    private final RefFileService refFileService;
    private final PendingUploadService pendingUploadService;
    private final FileService fileService;
    private final UserAccessService userAccessService;

    @Autowired
    public SpaceController(SessionService sessionService, SpaceService spaceService, RefFileService refFileService,
                           PendingUploadService pendingUploadService, FileService fileService, UserAccessService userAccessService) {
        this.sessionService = sessionService;
        this.spaceService = spaceService;
        this.refFileService = refFileService;
        this.pendingUploadService = pendingUploadService;
        this.fileService = fileService;
        this.userAccessService = userAccessService;
    }



    @RequestMapping(value = "/api/spaces/get", method = RequestMethod.POST)
    @ApiOperation(value = "Returns all spaces a user has access to.",
        response = GetSpacesResponseDto.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The response contains all spaces the user has access to."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
    })
    public @ResponseBody ResponseEntity<?>
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
    public @ResponseBody ResponseEntity<?>
    createSpace(@RequestBody CreateSpaceDto req){
        if (!sessionService.getSession(req.getAuth().getUserID(), req.getAuth().getSessionKey())){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        Long spaceID = spaceService.createSpace(req.getAuth().getUserID(), req.getReferenceFile(), req.isPrivate(),
                req.getUsersWriteAccess(), req.getUsersAuthAccess(), req.getAuthKey());

        return new ResponseEntity<>(spaceID, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/spaces/join", method = RequestMethod.PUT)
    @ApiOperation(value = "Adds the user to the space.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user was successfully added to the space."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "Either the space with given ID does not exist, it is private or the authorization key is wrong.")
    })
    public @ResponseBody ResponseEntity<?>
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

    @RequestMapping(value = "/api/spaces/quit/{spaceID}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Removes the user from the space.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user successfully quit the space."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 404, message = "The spaceID does not exist or you do not have access in the first place."),
            @ApiResponse(code = 406, message = "The user is the creator of the space and thus must delete the space manually.")
    })
    public @ResponseBody ResponseEntity<?>
    quitSpace(@RequestBody AuthWrapperDto req, @PathVariable Long spaceID){
        if (!sessionService.getSession(req.getAuth().getUserID(), req.getAuth().getSessionKey())){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        if (spaceService.checkCreator(spaceID, req.getAuth().getUserID())){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(null, userAccessService.removeAccess(req.getAuth().getUserID(), spaceID) ?
                HttpStatus.OK : HttpStatus.NOT_FOUND);

    }

    @RequestMapping(value = "/api/spaces/key", method = RequestMethod.POST)
    @ApiOperation(  value = "Returns the authentication key of a file.",
                    response = SpaceAuthKeyResponseDto.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The auth key is returned."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "Either the space with given ID does not exist, it is private or the authorization key is wrong."),
            @ApiResponse(code = 406, message = "User is not allowed to get the auth key.")
    })
    public @ResponseBody ResponseEntity<?>
    getAuthKey(@RequestBody SpaceAuthKeyDto req){
        if (!sessionService.getSession(req.getAuth().getUserID(), req.getAuth().getSessionKey())){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        if (spaceService.checkDeleted(req.getSpaceID()) ||
                !userAccessService.userHasAccess(req.getAuth().getUserID(), req.getSpaceID())){
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        if (!spaceService.userHasAuthKeyAccess(req.getSpaceID(), req.getAuth().getUserID())){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(spaceService.getSpaceAuthKey(req.getSpaceID()), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/spaces/config/{spaceID}", method = RequestMethod.POST)
    @ApiOperation(  value = "Returns the authentication key of a file.",
            response = ConfigureSpaceDto.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "The auth key is returned."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "Either the space with given ID does not exist, it is private or the authorization key is wrong."),
            @ApiResponse(code = 406, message = "User is not the creator.")
    })
    public @ResponseBody ResponseEntity<?>
    configureSpace(@RequestBody ConfigureSpaceDto req, @PathVariable Long spaceID){
        HttpStatus status = checkPrivilegeLevel(req.getAuth(), spaceID);
        if (status != null) return new ResponseEntity<>(null, status);
        if (req.getSharedSpace() != null) spaceService.changeSharedState(spaceID, req.getAuth().getUserID(), req.getSharedSpace());
        spaceService.configureSpace(spaceID, req.getUsersWriteAccess(), req.getUsersAuthAccess());
        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/api/spaces/{spaceID}/kickall", method = RequestMethod.POST)
    @ApiOperation(  value = "Returns the authentication key of a file.",
            response = GenericAuthDto.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The auth key is returned."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "Either the space with given ID does not exist, it is private or the user has no access."),
            @ApiResponse(code = 406, message = "User is not the creator.")
    })
    public @ResponseBody ResponseEntity<?>
    kickUsers(@RequestBody GenericAuthDto req, @PathVariable Long spaceID){
        HttpStatus status = checkPrivilegeLevel(req, spaceID);
        if (status != null) return new ResponseEntity<>(null, status);

        userAccessService.kickAll(spaceID, req.getUserID());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    // check whether user is logged in, has access and whether user is creator. If so, returns null
    private HttpStatus checkPrivilegeLevel(GenericAuthDto auth, Long spaceID){
        if (!sessionService.getSession(auth.getUserID(), auth.getSessionKey())){
            return HttpStatus.UNAUTHORIZED;
        }
        if (spaceService.checkDeleted(spaceID) ||
                !userAccessService.userHasAccess(auth.getUserID(), spaceID)){
            return HttpStatus.FORBIDDEN;
        }
        if (!spaceService.checkCreator(spaceID, auth.getUserID())){
            return HttpStatus.NOT_ACCEPTABLE;
        }
        return null;
    }



    @RequestMapping(value = "/api/spaces/delete/{spaceID}", method = RequestMethod.DELETE)
    @ApiOperation(  value = "Deletes the specified space if permitted.",
            response = SpaceAuthKeyResponseDto.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The space space was deleted successfully."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "Either the space with given ID does not exist."),
            @ApiResponse(code = 412, message = "Space is probably currently in deletion process.")
    })
    public @ResponseBody ResponseEntity<?>
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
