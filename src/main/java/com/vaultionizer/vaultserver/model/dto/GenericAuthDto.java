package com.vaultionizer.vaultserver.model.dto;

// A lot of requests need a sessionKey and the user's id
public class GenericAuthDto {
    private long userID;
    private String sessionKey;

    public long getUserID() {
        return userID;
    }

    public String getSessionKey() {
        return sessionKey;
    }
}
