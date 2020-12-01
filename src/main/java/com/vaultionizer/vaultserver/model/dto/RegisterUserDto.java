package com.vaultionizer.vaultserver.model.dto;

public class RegisterUserDto {
    private final String username;
    private final String key;
    private final String refFile;
    private String serverUser;
    private String serverAuthKey;

    public RegisterUserDto(String username, String key, String refFile) {
        this.username = username;
        this.key = key;
        this.refFile = refFile;
    }

    public RegisterUserDto(String username, String key, String refFile, String serverUser, String serverAuthKey) {
        this.username = username;
        this.key = key;
        this.refFile = refFile;
        this.serverUser = serverUser;
        this.serverAuthKey = serverAuthKey;
    }

    public String getServerUser() {
        return serverUser;
    }

    public String getUsername() {
        return username;
    }

    public String getKey() {
        return key;
    }

    public String getRefFile() {
        return refFile;
    }

    public String getServerAuthKey() {
        return serverAuthKey;
    }
}
