package com.vaultionizer.vaultserver.model.dto;

public class JoinSpaceDto {
    private GenericAuthDto auth;
    private Long spaceID;
    private String authKey;

    public GenericAuthDto getAuth() {
        return auth;
    }

    public JoinSpaceDto(GenericAuthDto auth, Long spaceID, String authKey) {
        this.auth = auth;
        this.spaceID = spaceID;
        this.authKey = authKey;
    }

    public Long getSpaceID() {
        return spaceID;
    }

    public String getAuthKey() {
        return authKey;
    }
}
