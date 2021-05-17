package com.vaultionizer.vaultserver.controllers;

import com.vaultionizer.vaultserver.helpers.AccessCheckerUtil;
import com.vaultionizer.vaultserver.model.dto.GenericAuthDto;
import com.vaultionizer.vaultserver.service.SessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "/api/session/", description = "Controller that allows renewing the session.")
@RestController
public class SessionController {
    private final SessionService sessionService;
    private final AccessCheckerUtil accessCheckerUtil;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
        accessCheckerUtil = new AccessCheckerUtil(sessionService, null, null);
    }

    @PutMapping(value = "/api/session/renew")
    @ApiOperation(value = "Renews the session with specified key.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The session has been renewed successfully."),
            @ApiResponse(code = 403, message = "The session either does not exist or has become invalid already."),
    })
    public @ResponseBody
    ResponseEntity<?>
    renewSession(@RequestHeader("xAuth") GenericAuthDto auth) {
        var status = accessCheckerUtil.checkAuthenticated(auth);
        if (status != null) return new ResponseEntity<>(null, status);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
