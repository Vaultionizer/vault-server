package com.vaultionizer.vaultserver.model.dto;

import java.time.Instant;

public class FetchDataResponseDto {
    private Instant timestamp;
    private String data;

    public FetchDataResponseDto(Instant timestamp, String data) {
        this.timestamp = timestamp;
        this.data = data;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getData() {
        return data;
    }
}
