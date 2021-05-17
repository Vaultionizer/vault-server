package com.vaultionizer.vaultserver.model.dto;

public class JoinSpaceDto {
    private String authKey;

    public JoinSpaceDto(String authKey) {
        this.authKey = authKey;
    }

    public String getAuthKey() {
        return authKey;
    }
}
