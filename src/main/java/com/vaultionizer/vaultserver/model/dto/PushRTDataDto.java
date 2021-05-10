package com.vaultionizer.vaultserver.model.dto;

public class PushRTDataDto {
    private GenericAuthDto auth;
    private String data;

    public GenericAuthDto getAuth() {
        return auth;
    }

    public String getData() {
        return data;
    }
}
