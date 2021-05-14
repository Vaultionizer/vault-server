package com.vaultionizer.vaultserver.model.dto;

public class UpdateRefFileDto {
    private Long spaceID;
    private String content;

    public UpdateRefFileDto(Long spaceID, String content) {
        this.spaceID = spaceID;
        this.content = content;
    }

    public Long getSpaceID() {
        return spaceID;
    }

    public String getContent() {
        return content;
    }
}
