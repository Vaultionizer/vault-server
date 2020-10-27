package com.vaultionizer.vaultserver.controllers;

import com.vaultionizer.vaultserver.model.db.SessionModel;
import com.vaultionizer.vaultserver.model.db.SpaceModel;
import com.vaultionizer.vaultserver.model.db.UserModel;
import com.vaultionizer.vaultserver.model.dto.*;
import com.vaultionizer.vaultserver.resource.SpaceRepository;
import com.vaultionizer.vaultserver.service.RefFileService;
import com.vaultionizer.vaultserver.service.SessionService;
import com.vaultionizer.vaultserver.service.SpaceService;
import com.vaultionizer.vaultserver.service.UserAccessService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Api(value = "/api/spaces/", description = "Controller that manages spaces.")
@RestController
public class SpaceController {
    private final SpaceRepository spaceRepository;
    private final SessionService sessionService;
    private final SpaceService spaceService;
    private final RefFileService refFileService;
    private final UserAccessService userAccessService;

    @Autowired
    public SpaceController(SpaceRepository spaceRepository, SessionService sessionService, SpaceService spaceService, RefFileService refFileService, UserAccessService userAccessService) {
        this.spaceRepository = spaceRepository;
        this.sessionService = sessionService;
        this.spaceService = spaceService;
        this.refFileService = refFileService;
        this.userAccessService = userAccessService;
    }

    @RequestMapping(value = "/api/spaces/getall", method = RequestMethod.POST)
    @ApiOperation(value = "Returns all spaces a user has access to.",
        response = GetSpacesResponseDto.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The response contains all spaces the user has access to."),
            @ApiResponse(code = 401, message = "The user either does not exist or the key is wrong. User is thus not authorized."),
    })
    @ResponseBody ResponseEntity<?>
    getAllSpaces(@RequestBody GenericAuthDto req){
        if (!sessionService.getSession(req.getUserID(), req.getSessionKey())){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(
                spaceService.getSpacesAccessible(req.getUserID()), HttpStatus.OK);
    }


    @RequestMapping(value = "/api/spaces/create", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new space.",
        response = Long.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The space was created successfully. The returned value is the space's ID."),
            @ApiResponse(code = 401, message = "The user either does not exist or the key is wrong."),
    })
    @ResponseBody ResponseEntity<?>
    createSpace(@RequestBody CreateSpaceDto req){
        if (!sessionService.getSession(req.getAuth().getUserID(), req.getAuth().getSessionKey())){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        var refFile = refFileService.addNewRefFile(req.getReferenceFile());
        SpaceModel model = spaceRepository.save(new SpaceModel(req.getAuth().getUserID(), refFile.getRefFileId(),
                req.isPrivate(), req.getAuthKey()));
        return new ResponseEntity<>(model.getSpaceID(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/spaces/join", method = RequestMethod.POST)
    @ApiOperation(value = "Adds the user to the space.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user was successfully added to the space."),
            @ApiResponse(code = 401, message = "The user either does not exist or the key is wrong. User is thus not authorized."),
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
}
