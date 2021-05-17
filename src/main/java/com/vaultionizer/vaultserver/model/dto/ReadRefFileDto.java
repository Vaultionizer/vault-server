package com.vaultionizer.vaultserver.model.dto;


import java.time.Instant;

public class ReadRefFileDto {
    private Instant lastRead;

    public ReadRefFileDto(Instant lastRead) {
        this.lastRead = lastRead;
    }

    public Instant getLastRead() {
        return lastRead;
    }
}
