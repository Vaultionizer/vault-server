package com.vaultionizer.vaultserver.model.dto;

public class SpaceAuthKeyResponseDto {
    private Long spaceID;
    private String authKey;

    public SpaceAuthKeyResponseDto(Long spaceID, String authKey) {
        this.spaceID = spaceID;
        this.authKey = authKey;
    }
}
