package com.vaultionizer.vaultserver.model.dto;

public class GetSpacesResponseDto {
    private Long spaceID;
    private boolean isPrivate;
    private boolean isCreator;

    public GetSpacesResponseDto(Long spaceID, boolean isPrivate, boolean isCreator) {
        this.spaceID = spaceID;
        this.isPrivate = isPrivate;
        this.isCreator = isCreator;
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

