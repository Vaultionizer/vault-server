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
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
    @ApiOperation("Returns all spaces a user has access to.")
    @ResponseBody ResponseEntity<?>
    getAllSpaces(@RequestBody GenericAuthDto req){
        if (!sessionService.getSession(req.getUserID(), req.getSessionKey())){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<ArrayList<GetSpacesResponseDto>>(
                spaceService.getSpacesAccessible(req.getUserID()), HttpStatus.ACCEPTED);
    }


    @RequestMapping(value = "/api/spaces/create", method = RequestMethod.POST)
    @ApiOperation("Creates a new space.")
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
    @ApiOperation("Adds the user to the space.")
    @ResponseBody ResponseEntity<?>
    joinSpace(@RequestBody JoinSpaceDto req){
        if (!sessionService.getSession(req.getAuth().getUserID(), req.getAuth().getSessionKey())){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        if (spaceService.checkSpaceCredentials(req.getSpaceID(), req.getAuthKey())){
            userAccessService.addUserAccess(req.getSpaceID(), req.getAuth().getUserID());
            return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }
}
