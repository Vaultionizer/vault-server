package com.vaultionizer.vaultserver.controllers;

import com.vaultionizer.vaultserver.helpers.Config;
import com.vaultionizer.vaultserver.model.db.SessionModel;
import com.vaultionizer.vaultserver.model.db.UserModel;
import com.vaultionizer.vaultserver.model.dto.LoginUserDto;
import com.vaultionizer.vaultserver.model.dto.LoginUserResponseDto;
import com.vaultionizer.vaultserver.model.dto.RegisterUserDto;
import com.vaultionizer.vaultserver.model.dto.RegisterUserResponseDto;
import com.vaultionizer.vaultserver.resource.UserRepository;
import com.vaultionizer.vaultserver.service.SessionService;
import com.vaultionizer.vaultserver.service.SpaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "/api/users/", description = "Controller that manages user interaction.")
public class UserController {

    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final SpaceService spaceService;

    @Autowired
    public UserController(UserRepository userRepository, SessionService sessionService, SpaceService spaceService) {
        this.userRepository = userRepository;
        this.sessionService = sessionService;
        this.spaceService = spaceService;
    }

    @RequestMapping(value = "/api/users/create", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new user, a new private space and adds a session.",
            response = RegisterUserResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The user was created successfully. The response is a session key and the newly created user's ID."),
            @ApiResponse(code = 400, message = "The values for key or the reference file does not match the constraints."),
    })
    @ResponseBody ResponseEntity<?>
    createUser(@RequestBody RegisterUserDto req){
        if (req.getKey() == null || req.getRefFile() == null ||
                req.getKey().length() < Config.MIN_USER_KEY_LENGTH || req.getRefFile().length() == 0){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        UserModel userModel = userRepository.save(new UserModel(req.getKey()));
        spaceService.addPrivateSpace(userModel.getId(), req.getRefFile());
        SessionModel sessionModel = sessionService.addSession(userModel.getId());
        return new ResponseEntity<>(new RegisterUserResponseDto(userModel.getId(), sessionModel.getSessionKey()),
                HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/users/login", method = RequestMethod.POST)
    @ApiOperation(value = "Logs the user in and returns a session.",
            response = LoginUserResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user was signed in successfully. The response is a session key."),
            @ApiResponse(code = 401, message = "The user authorization failed."),
    })
    @ResponseBody ResponseEntity<?>
    loginUser(@RequestBody LoginUserDto req){
        if (userRepository.checkCredentials(req.getUserID(), req.getKey()) != 1){
            // no user has that id in combination with the key
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        SessionModel model = sessionService.addSession(req.getUserID());

        return new ResponseEntity<>(
                new LoginUserResponseDto(req.getUserID(), model.getSessionKey()),
                HttpStatus.OK);
    }

}
