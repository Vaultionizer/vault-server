package com.vaultionizer.vaultserver.testdata;

import com.vaultionizer.vaultserver.model.dto.LoginUserDto;
import com.vaultionizer.vaultserver.model.dto.LoginUserResponseDto;
import com.vaultionizer.vaultserver.model.dto.RegisterUserDto;

public class UserTestData {
    public static final RegisterUserDto[] registerData = new RegisterUserDto[]{
            new RegisterUserDto("", null, null),    // key and ref file are null
            new RegisterUserDto("","", ""),        // key and ref file are empty
            new RegisterUserDto("","-----", "---"),// key is too short
            new RegisterUserDto("1234",
                    new String("--------|--------|--------|--------|--------|--------|--------|--------"),
                    "test") // legitimate key (correct length)
    };

    public static final LoginUserResponseDto[] registerResponses = new LoginUserResponseDto[]{
            new LoginUserResponseDto(1L, "testSessionKey", "")
    };

    public static final LoginUserDto[] loginUser = new LoginUserDto[]{
            new LoginUserDto("username", "testpwd"), // wrong pwd
            new LoginUserDto("username", "testpwdisbadpwd!") // correct
    };

}
