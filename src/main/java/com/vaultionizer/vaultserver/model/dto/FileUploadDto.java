package com.vaultionizer.vaultserver.model.dto;

public class FileUploadDto {
    private int amountFiles;

    public FileUploadDto() {
    }

    public FileUploadDto(int amountFiles) {
        this.amountFiles = amountFiles;
    }

    public int getAmountFiles() {
        return amountFiles;
    }
}
