package com.vaultionizer.vaultserver.model.dto;

public class LoginUserResponseDto {
    private Long userID;
    private String sessionKey;

    public LoginUserResponseDto(Long userID, String sessionKey) {
        this.userID = userID;
        this.sessionKey = sessionKey;
    }

    public Long getUserID() {
        return userID;
    }

    public String getSessionKey() {
        return sessionKey;
    }
}
