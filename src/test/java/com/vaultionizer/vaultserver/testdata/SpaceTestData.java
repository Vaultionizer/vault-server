package com.vaultionizer.vaultserver.testdata;

import com.vaultionizer.vaultserver.model.dto.*;

public class SpaceTestData {
    public static final CreateSpaceDto[] createSpace = {
            new CreateSpaceDto(true, "", ""),
            new CreateSpaceDto(true, "0", "000")
    };
    public static final GenericAuthDto[] createSpaceAuths = {
            new GenericAuthDto(
                    1L,
                    "testSessionKey"
            ), new GenericAuthDto(
            1L,
            "correctTestSessionKey"
    )};


    public static final JoinSpaceDto[] joinSpaces = {
            new JoinSpaceDto("definitely wrong"),
            new JoinSpaceDto("definitely wrong"),
            new JoinSpaceDto( "thatWasTheAuthKey!")
    };

    public static final GenericAuthDto[] joinSpacesAuth = {
            new GenericAuthDto(1L, "testSessionKey"),
            new GenericAuthDto(1L, "correctTestSessionKey"),
            new GenericAuthDto(1L, "correctTestSessionKey")
    };

    public static final Long[] joinSpacesSpaceIDs = { 2L, 2L, 2L };

    public static final GenericAuthDto[] getAllSpaces = {
            new GenericAuthDto(1L, "definitely wrong"),
            new GenericAuthDto(1L, "correctTestSessionKey")
    };

    public static final Long[] getAuthKeysSpaceIds = {
            3L, 3L, 4L, 3L
    };

    public static final GenericAuthDto[] getAuthKeyCredentials = {
            new GenericAuthDto(1L, "definitely wrong"),
            new GenericAuthDto(1L, "correctTestSessionKey"),
            new GenericAuthDto(1L, "correctTestSessionKey"),
            new GenericAuthDto(2L, "correctTestSessionKey")
    };
}
