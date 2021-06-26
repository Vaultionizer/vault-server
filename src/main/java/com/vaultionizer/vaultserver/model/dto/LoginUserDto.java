package com.vaultionizer.vaultserver.model.dto;

public class LoginUserDto {
    private String username;
    private String key;

    public LoginUserDto() {
    }

    public LoginUserDto(String username, String key) {
        this.username = username;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getUsername() {
        return username;
    }
}
