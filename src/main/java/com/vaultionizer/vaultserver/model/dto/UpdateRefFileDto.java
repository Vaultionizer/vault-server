package com.vaultionizer.vaultserver.model.dto;

public class UpdateRefFileDto {
    private String content;

    public UpdateRefFileDto(Long spaceID, String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
