package com.vaultionizer.vaultserver.controllers;

import com.vaultionizer.vaultserver.helpers.AccessCheckerUtil;
import com.vaultionizer.vaultserver.model.dto.*;
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
    private final AccessCheckerUtil accessCheckerUtil;

    @Autowired
    public SpaceController(SessionService sessionService, SpaceService spaceService, RefFileService refFileService,
                           PendingUploadService pendingUploadService, FileService fileService, UserAccessService userAccessService) {
        this.sessionService = sessionService;
        this.spaceService = spaceService;
        this.refFileService = refFileService;
        this.pendingUploadService = pendingUploadService;
        this.fileService = fileService;
        this.userAccessService = userAccessService;
        accessCheckerUtil = new AccessCheckerUtil(sessionService, userAccessService, spaceService);
    }


    @GetMapping(value = "/api/space")
    @ApiOperation(value = "Returns all spaces a user has access to.",
            response = GetSpacesResponseDto.class,
            responseContainer = "List"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The response contains all spaces the user has access to."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
    })
    public @ResponseBody
    ResponseEntity<?>
    getAllSpaces(@RequestHeader("xAuth") GenericAuthDto auth) {
        if (!sessionService.getSession(auth.getUserID(), auth.getSessionKey())) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(
                spaceService.getSpacesAccessible(auth.getUserID()), HttpStatus.OK);
    }


    @PostMapping(value = "/api/space/create")
    @ApiOperation(value = "Creates a new space.",
            response = Long.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The space was created successfully. The returned value is the space's ID."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong."),
    })
    public @ResponseBody
    ResponseEntity<?>
    createSpace(@RequestBody CreateSpaceDto req, @RequestHeader("xAuth") GenericAuthDto auth) {
        if (!sessionService.getSession(auth.getUserID(), auth.getSessionKey())) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        Long spaceID = spaceService.createSpace(auth.getUserID(), req.getReferenceFile(), req.isPrivate(),
                req.getUsersWriteAccess(), req.getUsersAuthAccess(), req.getAuthKey());

        return new ResponseEntity<>(spaceID, HttpStatus.CREATED);
    }

    @PutMapping(value = "/api/space/{spaceID}/join")
    @ApiOperation(value = "Adds the user to the space.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user was successfully added to the space."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "Either the space with given ID does not exist, it is private or the authorization key is wrong.")
    })
    public @ResponseBody
    ResponseEntity<?>
    joinSpace(@RequestBody JoinSpaceDto req, @RequestHeader("xAuth") GenericAuthDto auth, @PathVariable Long spaceID) {
        if (!sessionService.getSession(auth.getUserID(), auth.getSessionKey())) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        if (spaceService.checkSpaceCredentials(spaceID, req.getAuthKey())) {
            userAccessService.addUserAccess(spaceID, auth.getUserID());
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @DeleteMapping(value = "/api/space/{spaceID}/quit")
    @ApiOperation(value = "Removes the user from the space.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user successfully quit the space."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 404, message = "The spaceID does not exist or you do not have access in the first place."),
            @ApiResponse(code = 406, message = "The user is the creator of the space and thus must delete the space manually.")
    })
    public @ResponseBody
    ResponseEntity<?>
    quitSpace(@PathVariable Long spaceID, @RequestHeader("xAuth") GenericAuthDto auth) {
        if (!sessionService.getSession(auth.getUserID(), auth.getSessionKey())) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        if (spaceService.checkCreator(spaceID, auth.getUserID())) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(null, userAccessService.removeAccess(auth.getUserID(), spaceID) ?
                HttpStatus.OK : HttpStatus.NOT_FOUND);

    }

    @GetMapping(value = "/api/space/{spaceID}/authkey")
    @ApiOperation(value = "Returns the authentication key of a file.",
            response = SpaceAuthKeyResponseDto.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The auth key is returned."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "Either the space with given ID does not exist, it is private or the authorization key is wrong."),
            @ApiResponse(code = 406, message = "User is not allowed to get the auth key."),
            @ApiResponse(code = 417, message = "Some other error occurred.")
    })
    public @ResponseBody
    ResponseEntity<?>
    getAuthKey(@RequestHeader("xAuth") GenericAuthDto auth, @PathVariable Long spaceID) {
        HttpStatus status = accessCheckerUtil.checkAuthKeyAccess(auth, spaceID);
        if (status != null) return new ResponseEntity<>(null, status);

        var authKey = spaceService.getSpaceAuthKey(spaceID);
        if (authKey.isEmpty()) return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        return new ResponseEntity<>(authKey.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/api/space/{spaceID}/config")
    @ApiOperation(value = "Returns the authentication key of a file.",
            response = ConfigureSpaceDto.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "The auth key is returned."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "Either the space with given ID does not exist, it is private or the authorization key is wrong."),
            @ApiResponse(code = 406, message = "User is not the creator.")
    })
    public @ResponseBody
    ResponseEntity<?>
    configureSpace(@RequestBody ConfigureSpaceDto req, @PathVariable Long spaceID, @RequestHeader("xAuth") GenericAuthDto auth) {
        HttpStatus status = accessCheckerUtil.checkPrivilegeLevel(auth, spaceID);
        if (status != null) return new ResponseEntity<>(null, status);
        if (req.getSharedSpace() != null)
            spaceService.changeSharedState(spaceID, auth.getUserID(), req.getSharedSpace());
        spaceService.configureSpace(spaceID, req.getUsersWriteAccess(), req.getUsersAuthAccess());
        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = "/api/space/{spaceID}/kickall")
    @ApiOperation(value = "Returns the authentication key of a file.",
            response = GenericAuthDto.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The auth key is returned."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "Either the space with given ID does not exist, it is private or the user has no access."),
            @ApiResponse(code = 406, message = "User is not the creator.")
    })
    public @ResponseBody
    ResponseEntity<?>
    kickUsers(@PathVariable Long spaceID, @RequestHeader("xAuth") GenericAuthDto auth) {
        HttpStatus status = accessCheckerUtil.checkPrivilegeLevel(auth, spaceID);
        if (status != null) return new ResponseEntity<>(null, status);

        userAccessService.kickAll(spaceID, auth.getUserID());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping(value = "/api/space/{spaceID}/authkey")
    @ApiOperation(value = "Changes the authentication key of a space.",
            response = ChangeAuthKeyDto.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The auth key was replaced."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "Either the space with given ID does not exist, it is private or the user has no access."),
            @ApiResponse(code = 406, message = "User is not the creator.")
    })
    public @ResponseBody
    ResponseEntity<?>
    changeAuthKey(@RequestBody ChangeAuthKeyDto req, @PathVariable Long spaceID, @RequestHeader("xAuth") GenericAuthDto auth) {
        HttpStatus status = accessCheckerUtil.checkPrivilegeLevel(auth, spaceID);
        if (status != null) return new ResponseEntity<>(null, status);

        spaceService.changeAuthKey(spaceID, req.getAuthKey());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping(value = "/api/space/{spaceID}/config")
    @ApiOperation(value = "Returns the configuration of a space.",
            response = GetSpacesResponseDto.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The config is returned."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "Either the space with given ID does not exist, it is private or the user has no access.")
    })
    public @ResponseBody
    ResponseEntity<?>
    getSpaceConfig(@PathVariable Long spaceID, @RequestHeader("xAuth") GenericAuthDto auth) {
        HttpStatus status = accessCheckerUtil.checkAccess(auth, spaceID);
        if (status != null) return new ResponseEntity<>(null, status);

        return new ResponseEntity<>(spaceService.getSpaceConfig(spaceID), HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/space/{spaceID}")
    @ApiOperation(value = "Deletes the specified space if permitted.",
            response = SpaceAuthKeyResponseDto.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The space space was deleted successfully."),
            @ApiResponse(code = 401, message = "The user either does not exist or the sessionKey is wrong. User is thus not authorized."),
            @ApiResponse(code = 403, message = "Either the space with given ID does not exist."),
            @ApiResponse(code = 412, message = "Space is probably currently in deletion process.")
    })
    public @ResponseBody
    ResponseEntity<?>
    deleteSpace(@PathVariable Long spaceID, @RequestHeader("xAuth") GenericAuthDto auth) {
        if (!sessionService.getSession(auth.getUserID(), auth.getSessionKey())) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        if (!userAccessService.userHasAccess(auth.getUserID(), spaceID) ||
                !spaceService.checkCreator(spaceID, auth.getUserID())) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        if (!spaceService.markSpaceDeleted(spaceID)) {
            return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
        }
        deleteSpaceRoutine(spaceID);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    public void deleteSpaceRoutine(Long spaceID) {
        userAccessService.deleteAllWithSpace(spaceID);

        fileService.deleteAllFilesInSpace(spaceID);

        pendingUploadService.deleteAllPendingUploads(spaceID);

        Long refFileID = spaceService.getRefFileID(spaceID);
        refFileService.deleteRefFile(refFileID);
        spaceService.deleteSpace(spaceID);
    }
}
