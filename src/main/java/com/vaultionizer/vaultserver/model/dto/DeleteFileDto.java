package com.vaultionizer.vaultserver.model.dto;

public class DeleteFileDto {
    private GenericAuthDto auth;
    private Long spaceID;
    private Long saveIndex;

    public GenericAuthDto getAuth() {
        return auth;
    }

    public Long getSpaceID() {
        return spaceID;
    }

    public Long getSaveIndex() {
        return saveIndex;
    }
}
