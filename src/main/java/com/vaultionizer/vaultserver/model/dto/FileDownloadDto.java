package com.vaultionizer.vaultserver.model.dto;

public class FileDownloadDto {
    private Long spaceID;
    private Long saveIndex;

    public FileDownloadDto() {
    }

    public FileDownloadDto(Long spaceID, Long saveIndex) {
        this.spaceID = spaceID;
        this.saveIndex = saveIndex;
    }

    public Long getSpaceID() {
        return spaceID;
    }

    public Long getSaveIndex() {
        return saveIndex;
    }
}
