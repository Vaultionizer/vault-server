package com.vaultionizer.vaultserver.controllers;

import com.vaultionizer.vaultserver.model.dto.AuthWrapperDto;
import com.vaultionizer.vaultserver.model.dto.GenericAuthDto;
import com.vaultionizer.vaultserver.service.SessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "/api/session/", description = "Controller that allows renewing the session.")
@RestController
public class SessionController {
    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @RequestMapping(value = "/api/session/renew", method = RequestMethod.PUT)
    @ApiOperation(value = "Renews the session with specified key.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The session has been renewed successfully."),
            @ApiResponse(code = 403, message = "The session either does not exist or has become invalid already."),
    })
    @ResponseBody
    ResponseEntity<?>
    renewSession(@RequestHeader("auth") GenericAuthDto auth){
        if (!sessionService.getSession(auth.getUserID(), auth.getSessionKey())){
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        } // if the session exists, the session has just indirectly been renewed.
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
