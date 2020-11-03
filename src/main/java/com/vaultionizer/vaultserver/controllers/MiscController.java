package com.vaultionizer.vaultserver.controllers;


import com.vaultionizer.vaultserver.helpers.Config;
import com.vaultionizer.vaultserver.model.db.SessionModel;
import com.vaultionizer.vaultserver.model.db.UserModel;
import com.vaultionizer.vaultserver.model.dto.GetVersionResponseDto;
import com.vaultionizer.vaultserver.model.dto.RegisterUserDto;
import com.vaultionizer.vaultserver.model.dto.RegisterUserResponseDto;
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
    @ResponseBody ResponseEntity<?>
    getVersion(){
        return new ResponseEntity<>(Config.VERSION,
                HttpStatus.OK);
    }
}