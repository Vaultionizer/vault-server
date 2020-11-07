package com.vaultionizer.vaultserver.model.dto;

// A lot of requests need a sessionKey and the user's id
public class GenericAuthDto {
    private final Long userID;
    private final String sessionKey;

    public GenericAuthDto(Long userID, String sessionKey) {
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
