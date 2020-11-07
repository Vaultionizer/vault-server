package com.vaultionizer.vaultserver.model.dto;

import java.sql.Timestamp;

public class ReadRefFileDto {
    private GenericAuthDto auth;
    private Long spaceID;
    private Timestamp lastRead;

    public ReadRefFileDto(GenericAuthDto auth, Long spaceID, Timestamp lastRead) {
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

    public Timestamp getLastRead() {
        return lastRead;
    }
}
