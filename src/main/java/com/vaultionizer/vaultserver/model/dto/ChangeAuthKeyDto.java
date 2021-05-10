package com.vaultionizer.vaultserver.model.dto;

public class ChangeAuthKeyDto {
    private final GenericAuthDto auth;
    private final String authKey;

    public ChangeAuthKeyDto(GenericAuthDto auth, String authKey) {
        this.auth = auth;
        this.authKey = authKey;
    }

    public GenericAuthDto getAuth() {
        return auth;
    }

    public String getAuthKey() {
        return authKey;
    }
}
