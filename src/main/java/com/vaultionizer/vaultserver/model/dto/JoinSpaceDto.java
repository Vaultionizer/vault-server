package com.vaultionizer.vaultserver.model.dto;

public class JoinSpaceDto {
    private Long spaceID;
    private String authKey;

    public JoinSpaceDto(Long spaceID, String authKey) {
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
