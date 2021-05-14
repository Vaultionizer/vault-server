package com.vaultionizer.vaultserver.model.dto;

public class ChangeAuthKeyDto {
    private final String authKey;

    public ChangeAuthKeyDto(String authKey) {
        this.authKey = authKey;
    }

    public String getAuthKey() {
        return authKey;
    }
}
