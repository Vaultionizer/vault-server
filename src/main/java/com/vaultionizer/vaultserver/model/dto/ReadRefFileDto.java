package com.vaultionizer.vaultserver.model.dto;


import java.time.Instant;

public class ReadRefFileDto {
    private Long lastRead;

    public ReadRefFileDto() {
    }

    public ReadRefFileDto(Long lastRead) {
        this.lastRead = lastRead;
    }

    public Long getLastRead() {
        return lastRead;
    }
}
