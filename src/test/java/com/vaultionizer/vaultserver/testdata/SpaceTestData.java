package com.vaultionizer.vaultserver.testdata;

import com.vaultionizer.vaultserver.model.dto.*;

public class SpaceTestData {
    public static final CreateSpaceDto[] createSpace = {
            new CreateSpaceDto(new GenericAuthDto(
                    1L,
                    "testSessionKey"
            ), true, "", ""),
            new CreateSpaceDto(new GenericAuthDto(
                    1L,
                    "correctTestSessionKey"
            ), true, "0", "000")
    };


    public static final JoinSpaceDto[] joinSpaces = {
            new JoinSpaceDto(new GenericAuthDto(
                    1L,
                    "testSessionKey"
            ), 2L, "definitely wrong"),
            new JoinSpaceDto(new GenericAuthDto(
                    1L,
                    "correctTestSessionKey"
            ), 2L, "definitely wrong"),
            new JoinSpaceDto(new GenericAuthDto(
                    1L,
                    "correctTestSessionKey"
            ), 2L, "thatWasTheAuthKey!")
    };

    public static final AuthWrapperDto[] getAllSpaces = {
        new AuthWrapperDto(new GenericAuthDto(1L, "definitely wrong")),
        new AuthWrapperDto(new GenericAuthDto(1L, "correctTestSessionKey"))
    };

    public static final Long[] getAuthKeys = {
            3L, 3L, 4L, 3L
    };

    public static final GenericAuthDto[] getAuthKeyCredentials = {
            new GenericAuthDto(1L, "definitely wrong"),
            new GenericAuthDto(1L, "correctTestSessionKey"),
            new GenericAuthDto(1L, "correctTestSessionKey"),
            new GenericAuthDto(2L, "correctTestSessionKey")
    };
}
