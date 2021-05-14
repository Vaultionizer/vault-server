package com.vaultionizer.vaultserver.model.dto;


import java.time.Instant;

public class ReadRefFileDto {
    private Long spaceID;
    private Instant lastRead;

    public ReadRefFileDto(Long spaceID, Instant lastRead) {
        this.spaceID = spaceID;
        this.lastRead = lastRead;
    }

    public Long getSpaceID() {
        return spaceID;
    }

    public Instant getLastRead() {
        return lastRead;
    }
}
