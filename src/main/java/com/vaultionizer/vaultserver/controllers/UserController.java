package com.vaultionizer.vaultserver.controllers;

import com.vaultionizer.vaultserver.helpers.Config;
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

import java.util.ArrayList;

@RestController
@Api(value = "/api/users/", description = "Controller that manages user interaction.")
public class UserController {

    private final UserService userService;
    private final SessionService sessionService;
    private final SpaceService spaceService;
    private final SpaceController spaceController;
    private final UserAccessService userAccessService;
    private final PendingUploadService pendingUploadService;

    @Autowired
    public UserController(UserService userService, SessionService sessionService,
                          SpaceService spaceService, SpaceController spaceController,
                          UserAccessService userAccessService, PendingUploadService pendingUploadService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.spaceService = spaceService;
        this.spaceController = spaceController;
        this.userAccessService = userAccessService;
        this.pendingUploadService = pendingUploadService;
    }


    @PostMapping(value = "/api/user/create")
    @ApiOperation(value = "Creates a new user, a new private space and adds a session.",
            response = LoginUserResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The user was created successfully. The response is a session key and the newly created user's ID."),
            @ApiResponse(code = 400, message = "The values for key or the reference file does not match the constraints."),
            @ApiResponse(code = 403, message = "The server credentials are wrong."),
            @ApiResponse(code = 409, message = "The username is in use.")
    })
    public @ResponseBody
    ResponseEntity<?>
    createUser(@RequestBody RegisterUserDto req) {
        if (Config.VERSION.isHasAuthKey() &&
                (req.getServerUser() == null || req.getServerAuthKey() == null ||
                        !req.getServerUser().equals(Config.SERVER_USER) || !req.getServerAuthKey().equals(Config.SERVER_AUTH))) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        if (req.getKey() == null || req.getRefFile() == null ||
                req.getKey().length() < Config.MIN_USER_KEY_LENGTH || req.getRefFile().length() == 0 ||
                req.getUsername() == null || req.getUsername().length() < Config.MIN_USERNAME_LENGTH
        ) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Long userID = userService.createUser(req.getUsername(), req.getKey());
        if (userID == null) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        spaceService.createSpace(userID, req.getRefFile(), true, false, false, null);
        return new ResponseEntity<>(sessionService.addSession(userID), HttpStatus.CREATED);
    }

    @PostMapping(value = "/api/user/login")
    @ApiOperation(value = "Logs the user in and returns a session.",
            response = LoginUserResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user was signed in successfully. The response is a session key."),
            @ApiResponse(code = 401, message = "The user authorization failed.")
    })
    public @ResponseBody
    ResponseEntity<?>
    loginUser(@RequestBody LoginUserDto req) {
        Long userID = userService.getUserIDCheckCredentials(req.getUsername(), req.getKey());
        if (userID == -1) {
            // no user has that id in combination with the key
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(sessionService.addSession(userID), HttpStatus.OK);
    }

    @PutMapping(value = "/api/user/logout")
    @ApiOperation(value = "Logs the user out.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user was logged out successfully.")
    })
    public @ResponseBody
    ResponseEntity<?>
    logoutUser(@RequestBody AuthWrapperDto req) {
        GenericAuthDto auth = req.getAuth();
        sessionService.deleteSession(auth.getUserID(), auth.getSessionKey());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/user")
    @ApiOperation(value = "Deletes the specified user and all spaces the user created.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The deletion process was successful."),
            @ApiResponse(code = 401, message = "The user authorization failed.")
    })
    public @ResponseBody
    ResponseEntity<?>
    deleteUser(@RequestHeader("auth") GenericAuthDto auth) {
        if (!sessionService.getSession(auth.getUserID(), auth.getSessionKey())) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        userService.setDeleted(auth.getUserID());
        pendingUploadService.deletePendingUploadsByUser(auth.getUserID());
        sessionService.deleteAllSessionsWithUser(auth.getUserID());
        ArrayList<GetSpacesResponseDto> spaces = spaceService.getSpacesAccessible(auth.getUserID());
        spaces.forEach(space -> {
            if (space.isCreator()) {
                spaceController.deleteSpaceRoutine(space.getSpaceID());
            }
        });
        userAccessService.deleteAllWithUser(auth.getUserID());
        userService.setDeletionDone(auth.getUserID());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
