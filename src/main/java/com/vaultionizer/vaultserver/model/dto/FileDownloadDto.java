package com.vaultionizer.vaultserver.model.dto;

public class FileDownloadDto {
    private GenericAuthDto auth;
    private Long spaceID;
    private Long saveIndex;

    public FileDownloadDto() {
    }

    public FileDownloadDto(GenericAuthDto auth, Long spaceID, Long saveIndex) {
        this.auth = auth;
        this.spaceID = spaceID;
        this.saveIndex = saveIndex;
    }

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
