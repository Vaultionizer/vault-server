package com.vaultionizer.vaultserver.model.dto;

public class RegisterUserResponseDto {
    private Long userID;
    private String sessionKey;

    public RegisterUserResponseDto(Long userID, String sessionKey) {
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
