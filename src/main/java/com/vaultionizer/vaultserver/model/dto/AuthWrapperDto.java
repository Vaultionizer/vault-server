package com.vaultionizer.vaultserver.model.dto;

public class AuthWrapperDto {
    private GenericAuthDto auth;

    public AuthWrapperDto() {
    }

    public AuthWrapperDto(GenericAuthDto auth) {
        this.auth = auth;
    }

    public GenericAuthDto getAuth() {
        return auth;
    }

    public void setAuth(GenericAuthDto auth) {
        this.auth = auth;
    }
}
