package com.vaultionizer.vaultserver.model.dto;

public class FileUploadDto {
    private Long spaceID;
    private int amountFiles;

    public FileUploadDto(Long spaceID, int amountFiles) {
        this.spaceID = spaceID;
        this.amountFiles = amountFiles;
    }

    public Long getSpaceID() {
        return spaceID;
    }

    public int getAmountFiles() {
        return amountFiles;
    }
}
