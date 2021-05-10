package com.vaultionizer.vaultserver.model.dto;

public class GetSpaceConfigResponseDto {
    private boolean isPrivate;
    private boolean usersHaveWriteAccess;
    private boolean usersCanInvite;

    public GetSpaceConfigResponseDto(boolean isPrivate, boolean usersHaveWriteAccess, boolean usersCanInvite) {
        this.isPrivate = isPrivate;
        this.usersHaveWriteAccess = usersHaveWriteAccess;
        this.usersCanInvite = usersCanInvite;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public boolean isUsersHaveWriteAccess() {
        return usersHaveWriteAccess;
    }

    public boolean isUsersCanInvite() {
        return usersCanInvite;
    }
}
