package com.vaultionizer.vaultserver.controllers;

import com.vaultionizer.vaultserver.model.db.SessionModel;
import com.vaultionizer.vaultserver.model.db.UserModel;
import com.vaultionizer.vaultserver.model.dto.LoginUserDto;
import com.vaultionizer.vaultserver.model.dto.LoginUserResponseDto;
import com.vaultionizer.vaultserver.model.dto.RegisterUserDto;
import com.vaultionizer.vaultserver.model.dto.RegisterUserResponseDto;
import com.vaultionizer.vaultserver.resource.UserRepository;
import com.vaultionizer.vaultserver.service.SessionService;
import com.vaultionizer.vaultserver.service.SpaceService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private SessionService sessionService;
    private SpaceService spaceService;


    @Autowired
    public UserController(UserRepository userRepository, SessionService sessionService, SpaceService spaceService) {
        this.userRepository = userRepository;
        this.sessionService = sessionService;
        this.spaceService = spaceService;
    }

    @RequestMapping(value = "/api/users/create", method = RequestMethod.POST)
    @ApiOperation("Creates a new user, a new private space and adds a session.")
    @ResponseBody RegisterUserResponseDto
    createUser(@RequestBody RegisterUserDto req){
        UserModel userModel = userRepository.save(new UserModel(req.getKey()));
        spaceService.addPrivateSpace(userModel.getId(), req.getRefFile());
        SessionModel sessionModel = sessionService.addSession(userModel.getId());
        return new RegisterUserResponseDto(userModel.getId(), sessionModel.getSessionKey());
    }

    @RequestMapping(value = "/api/users/login", method = RequestMethod.POST)
    @ResponseBody ResponseEntity<?>
    loginUser(@RequestBody LoginUserDto req){
        if (userRepository.checkCredentials(req.getUserID(), req.getKey()) != 1){
            // no user has that id in combination with the key
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        SessionModel model = sessionService.addSession(req.getUserID());

        return new ResponseEntity<LoginUserResponseDto>(
                new LoginUserResponseDto(req.getUserID(), model.getSessionKey()),
                HttpStatus.OK);
    }

}
