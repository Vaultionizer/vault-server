package com.vaultionizer.vaultserver.model.dto;

public class SpaceAuthKeyResponseDto {
    private Long spaceID;
    private String authKey;

    public SpaceAuthKeyResponseDto() {
    }

    public SpaceAuthKeyResponseDto(Long spaceID, String authKey) {
        this.spaceID = spaceID;
        this.authKey = authKey;
    }

    public Long getSpaceID() {
        return spaceID;
    }

    public String getAuthKey() {
        return authKey;
    }
}
