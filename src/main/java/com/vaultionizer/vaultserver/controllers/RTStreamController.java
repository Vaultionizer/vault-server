package com.vaultionizer.vaultserver.controllers;

import com.vaultionizer.vaultserver.helpers.Config;
import com.vaultionizer.vaultserver.model.dto.AuthWrapperDto;
import com.vaultionizer.vaultserver.model.dto.GenericAuthDto;
import com.vaultionizer.vaultserver.model.dto.PushRTDataDto;
import com.vaultionizer.vaultserver.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "/api/stream/", description = "Controller that handles realtime stream requests.")
@RestController
public class RTStreamController {
    private final SessionService sessionService;
    private final UserAccessService userAccessService;
    private final RTStreamService rtStreamService;
    private final RTStreamDataService rtStreamDataService;


    @Autowired
    public RTStreamController(SessionService sessionService, UserAccessService userAccessService,
                              RTStreamService rtStreamService, RTStreamDataService rtStreamDataService) {
        this.sessionService = sessionService;
        this.userAccessService = userAccessService;
        this.rtStreamService = rtStreamService;
        this.rtStreamDataService = rtStreamDataService;
    }

    @RequestMapping(value = "/api/stream/register/{spaceID}", method = RequestMethod.POST)
    @ApiOperation(value = "Register a new stream for a given spaceID.",
            response = Long.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The stream was created successfully."),
            @ApiResponse(code = 400, message = "The spaceID is malformed."),
            @ApiResponse(code = 401, message = "The user authorization failed."),
            @ApiResponse(code = 403, message = "The user has no access to the space specified."),
            @ApiResponse(code = 412, message = "Realtime is disabled on this server.")
    })
    public @ResponseBody
    ResponseEntity<?>
    registerStream(@RequestBody AuthWrapperDto req, @PathVariable("spaceID") Long spaceID){
        if (Config.REALTIME_DISABLED) return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
        if (spaceID == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        GenericAuthDto auth = req.getAuth();
        if (!sessionService.getSession(auth.getUserID(), auth.getSessionKey())){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        if (userAccessService.userHasAccess(auth.getUserID(), spaceID)){
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        Long streamID = rtStreamService.createStream(spaceID);
        return new ResponseEntity<>(streamID, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/stream/push/{streamID}", method = RequestMethod.POST)
    @ApiOperation(value = "Push data to a specific stream.",
            response = Long.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The data was pushed successfully."),
            @ApiResponse(code = 401, message = "The user authorization failed."),
            @ApiResponse(code = 403, message = "The user has no access to the stream specified."),
            @ApiResponse(code = 412, message = "Realtime is disabled on this server."),
            @ApiResponse(code = 425, message = "The minimum delay was not met.")
    })
    public @ResponseBody
    ResponseEntity<?>
    pushData(@RequestBody PushRTDataDto req, @PathVariable("streamID") Long streamID){
        var res = checkValidRequest(req.getAuth(), streamID);
        if (res != null) return res;

        boolean success = rtStreamDataService.pushData(streamID, req.getData());
        if (!success) return new ResponseEntity<>(null, HttpStatus.TOO_EARLY);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/stream/delete/{streamID}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Deletes the stream specified.",
            response = Long.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user was signed in successfully. The response is a session key."),
            @ApiResponse(code = 401, message = "The user authorization failed."),
            @ApiResponse(code = 403, message = "The user has no access to the stream."),
            @ApiResponse(code = 412, message = "Realtime is disabled on this server.")
    })
    public @ResponseBody
    ResponseEntity<?>
    deleteStream(@RequestBody AuthWrapperDto req, @PathVariable("streamID") Long streamID){
        var res = checkValidRequest(req.getAuth(), streamID);
        if (res != null) return res;
        rtStreamService.deleteStream(streamID);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }



    @RequestMapping(value = "/api/stream/fetch/{streamID}/{limit}", method = RequestMethod.POST)
    @ApiOperation(value = "Fetches a specific amount of entries of data from a stream.",
            response = Long.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The values were fetched successfully."),
            @ApiResponse(code = 401, message = "The user authorization failed."),
            @ApiResponse(code = 403, message = "The user has no access to the stream."),
            @ApiResponse(code = 412, message = "Realtime is disabled on this server.")
    })
    public @ResponseBody
    ResponseEntity<?>
    fetchData(@RequestBody AuthWrapperDto req, @PathVariable("streamID") Long streamID, @PathVariable("limit") Long limit){
        var res = checkValidRequest(req.getAuth(), streamID);
        if (res != null) return res;

        // TODO
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    private ResponseEntity<?> checkValidRequest(GenericAuthDto auth, Long streamID){
        if (Config.REALTIME_DISABLED) return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
        if (!sessionService.getSession(auth.getUserID(), auth.getSessionKey())){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        Long spaceID = rtStreamService.getSpaceID(streamID);
        if (spaceID == null || !userAccessService.userHasAccess(auth.getUserID(),spaceID)){
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        return null;
    }
}
