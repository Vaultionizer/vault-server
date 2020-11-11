package com.vaultionizer.vaultserver.model.dto;

public class RegisterUserDto {
    private String username;
    private String key;
    private String refFile;


    public RegisterUserDto(String username, String key, String refFile) {
        this.username = username;
        this.key = key;
        this.refFile = refFile;
    }

    public String getKey() {
        return key;
    }

    public String getRefFile() {
        return refFile;
    }

    public String getUsername() {
        return username;
    }
}
