package com.vaultionizer.vaultserver.model.dto;

public class SpaceAuthKeyDto {
    private Long spaceID;

    public SpaceAuthKeyDto(Long spaceID) {
        this.spaceID = spaceID;
    }

    public Long getSpaceID() {
        return spaceID;
    }
}
