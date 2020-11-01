package com.vaultionizer.vaultserver.model.dto;

public class SpaceAuthKeyDto {
    private GenericAuthDto auth;
    private Long spaceID;

    public GenericAuthDto getAuth() {
        return auth;
    }

    public Long getSpaceID() {
        return spaceID;
    }
}
