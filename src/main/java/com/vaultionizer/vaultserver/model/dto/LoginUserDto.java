package com.vaultionizer.vaultserver.model.dto;

public class LoginUserDto {
    private Long userID;
    private String key;

    public Long getUserID() {
        return userID;
    }

    public String getKey() {
        return key;
    }

    public LoginUserDto(Long userID, String key) {
        this.userID = userID;
        this.key = key;
    }
}
