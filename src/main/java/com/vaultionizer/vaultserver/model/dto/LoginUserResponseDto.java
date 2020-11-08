package com.vaultionizer.vaultserver.model.dto;

public class LoginUserResponseDto {
    private Long userID;
    private String sessionKey;
    private String websocketToken;

    public LoginUserResponseDto(Long userID, String sessionKey, String websocketToken) {
        this.userID = userID;
        this.sessionKey = sessionKey;
        this.websocketToken = websocketToken;
    }

    public Long getUserID() {
        return userID;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public String getWebsocketToken() {
        return websocketToken;
    }
}
