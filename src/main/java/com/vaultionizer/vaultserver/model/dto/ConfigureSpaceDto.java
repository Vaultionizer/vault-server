package com.vaultionizer.vaultserver.model.dto;

public class ConfigureSpaceDto {
    private GenericAuthDto auth;
    private boolean usersWriteAccess;
    private boolean usersAuthAccess;
    private Boolean sharedSpace;

    public ConfigureSpaceDto(GenericAuthDto auth, boolean usersWriteAccess, boolean usersAuthAccess, Boolean sharedSpace) {
        this.auth = auth;
        this.usersWriteAccess = usersWriteAccess;
        this.usersAuthAccess = usersAuthAccess;
        this.sharedSpace = sharedSpace;
    }

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
