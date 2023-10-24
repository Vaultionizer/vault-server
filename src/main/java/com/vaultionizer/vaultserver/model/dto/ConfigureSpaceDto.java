package com.vaultionizer.vaultserver.model.dto;

public class ConfigureSpaceDto {
    private boolean usersWriteAccess;
    private boolean usersAuthAccess;
    private Boolean sharedSpace;

    public ConfigureSpaceDto() {
    }

    public ConfigureSpaceDto(boolean usersWriteAccess, boolean usersAuthAccess, Boolean sharedSpace) {
        this.usersWriteAccess = usersWriteAccess;
        this.usersAuthAccess = usersAuthAccess;
        this.sharedSpace = sharedSpace;
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
