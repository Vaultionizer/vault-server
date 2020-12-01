package com.vaultionizer.vaultserver.model.dto;

public class CheckAuthenticatedDto {
    private final String serverUser;
    private final String serverAuthKey;

    public CheckAuthenticatedDto(String serverUser, String serverAuthKey) {
        this.serverUser = serverUser;
        this.serverAuthKey = serverAuthKey;
    }

    public String getServerUser() {
        return serverUser;
    }

    public String getServerAuthKey() {
        return serverAuthKey;
    }
}
