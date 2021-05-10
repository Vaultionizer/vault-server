package com.vaultionizer.vaultserver.model.dto;

public class GetSpacesResponseDto {
    private Long spaceID;
    private boolean isPrivate;
    private boolean isCreator;
    private boolean hasWriteAccess;
    private boolean hasAuthKeyAccess;

    public GetSpacesResponseDto(Long spaceID, boolean isPrivate, boolean isCreator, boolean hasWriteAccess, boolean hasAuthKeyAccess) {
        this.spaceID = spaceID;
        this.isPrivate = isPrivate;
        this.isCreator = isCreator;
        this.hasWriteAccess = hasWriteAccess;
        this.hasAuthKeyAccess = hasAuthKeyAccess;
    }

    public boolean isHasWriteAccess() {
        return hasWriteAccess;
    }

    public boolean isHasAuthKeyAccess() {
        return hasAuthKeyAccess;
    }

    public Long getSpaceID() {
        return spaceID;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public boolean isCreator() {
        return isCreator;
    }
}

