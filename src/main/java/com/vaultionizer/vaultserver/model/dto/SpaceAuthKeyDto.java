package com.vaultionizer.vaultserver.model.dto;

public class SpaceAuthKeyDto {
    private GenericAuthDto auth;
    private Long spaceID;

    public SpaceAuthKeyDto(GenericAuthDto auth, Long spaceID) {
        this.auth = auth;
        this.spaceID = spaceID;
    }

    public GenericAuthDto getAuth() {
        return auth;
    }

    public Long getSpaceID() {
        return spaceID;
    }
}
