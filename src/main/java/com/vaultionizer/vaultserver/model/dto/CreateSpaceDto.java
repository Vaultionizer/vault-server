package com.vaultionizer.vaultserver.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateSpaceDto {
    private boolean isPrivate;
    private boolean usersWriteAccess;
    private boolean usersAuthAccess;
    private String authKey;
    private String referenceFile;

    public CreateSpaceDto() {
    }

    public boolean getUsersWriteAccess() {
        return usersWriteAccess;
    }

    public boolean getUsersAuthAccess() {
        return usersAuthAccess;
    }

    @JsonProperty("isPrivate")
    public boolean isPrivate() {
        return isPrivate;
    }

    public String getAuthKey() {
        return authKey;
    }

    public String getReferenceFile() {
        return referenceFile;
    }

    public CreateSpaceDto(boolean isPrivate, String authKey, String referenceFile) {
        this.isPrivate = isPrivate;
        this.authKey = authKey;
        this.referenceFile = referenceFile;
    }
}
