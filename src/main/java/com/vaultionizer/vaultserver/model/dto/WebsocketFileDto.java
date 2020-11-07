package com.vaultionizer.vaultserver.model.dto;

public class WebsocketFileDto {
    private String content;

    public WebsocketFileDto() {
    }

    public WebsocketFileDto(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
