package com.vaultionizer.vaultserver.model.dto;

public class ChangeAuthKeyDto {
    private String authKey;

    public ChangeAuthKeyDto() {
    }

    public ChangeAuthKeyDto(String authKey) {
        this.authKey = authKey;
    }

    public String getAuthKey() {
        return authKey;
    }
}
