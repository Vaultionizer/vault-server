package com.vaultionizer.vaultserver.testdata;

import com.vaultionizer.vaultserver.model.dto.CreateSpaceDto;
import com.vaultionizer.vaultserver.model.dto.GenericAuthDto;
import com.vaultionizer.vaultserver.model.dto.JoinSpaceDto;
import com.vaultionizer.vaultserver.model.dto.SpaceAuthKeyDto;

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

    public static final GenericAuthDto[] getAllSpaces = {
        new GenericAuthDto(1L, "definitely wrong"),
        new GenericAuthDto(1L, "correctTestSessionKey")
    };

    public static final SpaceAuthKeyDto[] getAuthKeys = {
            new SpaceAuthKeyDto(new GenericAuthDto(1L, "definitely wrong"), 3L),
            new SpaceAuthKeyDto(new GenericAuthDto(1L, "correctTestSessionKey"), 3L),
            new SpaceAuthKeyDto(new GenericAuthDto(1L, "correctTestSessionKey"), 4L)
    };
}
