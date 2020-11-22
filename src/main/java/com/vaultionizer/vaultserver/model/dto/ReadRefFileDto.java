package com.vaultionizer.vaultserver.model.dto;


import java.time.Instant;

public class ReadRefFileDto {
    private GenericAuthDto auth;
    private Long spaceID;
    private Instant lastRead;

    public ReadRefFileDto(GenericAuthDto auth, Long spaceID, Instant lastRead) {
        this.auth = auth;
        this.spaceID = spaceID;
        this.lastRead = lastRead;
    }

    public GenericAuthDto getAuth() {
        return auth;
    }

    public Long getSpaceID() {
        return spaceID;
    }

    public Instant getLastRead() {
        return lastRead;
    }
}
