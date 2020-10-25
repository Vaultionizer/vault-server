package com.vaultionizer.vaultserver.model.dto;

public class CreateSpaceDto {
    private GenericAuthDto auth;
    private boolean isPrivate;
    private String authKey;
    private String referenceFile;

    public GenericAuthDto getAuth() {
        return auth;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public String getAuthKey() {
        return authKey;
    }

    public String getReferenceFile() {
        return referenceFile;
    }
}
