package com.vaultionizer.vaultserver.model.dto;

public class ConfigureSpaceDto {
    private GenericAuthDto auth;
    private boolean usersWriteAccess;
    private boolean usersAuthAccess;

    public GenericAuthDto getAuth() {
        return auth;
    }

    public boolean getUsersWriteAccess() {
        return usersWriteAccess;
    }

    public boolean getUsersAuthAccess() {
        return usersAuthAccess;
    }
}
