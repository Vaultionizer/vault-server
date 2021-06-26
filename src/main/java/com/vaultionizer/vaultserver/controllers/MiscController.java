package com.vaultionizer.vaultserver.controllers;


import com.vaultionizer.vaultserver.helpers.Config;
import com.vaultionizer.vaultserver.model.dto.CheckAuthenticatedDto;
import com.vaultionizer.vaultserver.model.dto.GetVersionResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "/api/misc/", description = "Controller that handles miscellaneous requests.")
@RestController
public class MiscController {
    @RequestMapping(value = "/api/misc/version", method = RequestMethod.GET)
    @ApiOperation(value = "Returns the version of the server and can be used to test whether the server is up.",
            response = GetVersionResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The server's version is returned.")
    })
    public @ResponseBody
    ResponseEntity<?>
    getVersion() {
        return new ResponseEntity<>(Config.VERSION,
                HttpStatus.OK);
    }

    @PostMapping(value = "/api/misc/checkAuthenticated")
    @ApiOperation(value = "Returns whether the authentication for the server is correct.")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "The authentication is correct."),
            @ApiResponse(code = 403, message = "The authentication failed.")
    })
    public @ResponseBody
    ResponseEntity<?>
    checkAuthenticated(@RequestBody CheckAuthenticatedDto req) {
        return new ResponseEntity<>((!Config.VERSION.isHasAuthKey() ||
                (Config.SERVER_USER.equals(req.getServerUser()) && Config.SERVER_AUTH.equals(req.getServerAuthKey())))
                ? HttpStatus.ACCEPTED : HttpStatus.FORBIDDEN);
    }
}
