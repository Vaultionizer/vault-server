package com.vaultionizer.vaultserver.model.dto;

public class UpdateRefFileDto {
    private GenericAuthDto auth;
    private Long spaceID;
    private String content;

    public UpdateRefFileDto(GenericAuthDto auth, Long spaceID, String content) {
        this.auth = auth;
        this.spaceID = spaceID;
        this.content = content;
    }

    public GenericAuthDto getAuth() {
        return auth;
    }

    public Long getSpaceID() {
        return spaceID;
    }

    public String getContent() {
        return content;
    }
}
