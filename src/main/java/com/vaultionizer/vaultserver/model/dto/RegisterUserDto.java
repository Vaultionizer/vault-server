package com.vaultionizer.vaultserver.model.dto;

public class RegisterUserDto {
    private String key;
    private String refFile;


    public RegisterUserDto(String key, String refFile) {
        this.key = key;
        this.refFile = refFile;
    }

    public String getKey() {
        return key;
    }

    public String getRefFile() {
        return refFile;
    }
}
