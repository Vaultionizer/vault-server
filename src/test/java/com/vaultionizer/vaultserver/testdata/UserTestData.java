package com.vaultionizer.vaultserver.testdata;

import com.vaultionizer.vaultserver.model.db.UserModel;
import com.vaultionizer.vaultserver.model.dto.RegisterUserDto;
import com.vaultionizer.vaultserver.model.dto.RegisterUserResponseDto;

import java.util.ArrayList;

public class UserTestData {
    public static final RegisterUserDto[] registerData = new RegisterUserDto[]{
            new RegisterUserDto(null, null),    // key and ref file are null
            new RegisterUserDto("", ""),        // key and ref file are empty
            new RegisterUserDto("-----", "---"),// key is too short
            new RegisterUserDto(
                    new String("--------|--------|--------|--------|--------|--------|--------|--------"),
                    "test") // legitimate key (correct length)
    };

    public static final RegisterUserResponseDto[] registerResponses = new RegisterUserResponseDto[]{
            new RegisterUserResponseDto(1L, "testSessionKey")
    };

}
