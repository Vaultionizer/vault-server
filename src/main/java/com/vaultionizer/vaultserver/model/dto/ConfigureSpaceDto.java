package com.vaultionizer.vaultserver.model.dto;

public class ConfigureSpaceDto {
    private GenericAuthDto auth;
    private boolean usersWriteAccess;
    private boolean usersAuthAccess;
    private Boolean sharedSpace;

    public GenericAuthDto getAuth() {
        return auth;
    }

    public Boolean getSharedSpace() {
        return sharedSpace;
    }

    public boolean getUsersWriteAccess() {
        return usersWriteAccess;
    }

    public boolean getUsersAuthAccess() {
        return usersAuthAccess;
    }
}
