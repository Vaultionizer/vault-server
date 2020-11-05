package com.vaultionizer.vaultserver.model.dto;

public class FileUploadDto {
    private GenericAuthDto auth;
    private Long spaceID;
    private int amountFiles;

    public FileUploadDto(GenericAuthDto auth, Long spaceID, int amountFiles) {
        this.auth = auth;
        this.spaceID = spaceID;
        this.amountFiles = amountFiles;
    }

    public GenericAuthDto getAuth() {
        return auth;
    }

    public Long getSpaceID() {
        return spaceID;
    }

    public int getAmountFiles() {
        return amountFiles;
    }
}
