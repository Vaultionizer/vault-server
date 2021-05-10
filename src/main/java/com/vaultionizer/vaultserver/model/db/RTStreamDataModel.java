package com.vaultionizer.vaultserver.model.db;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
public class RTStreamDataModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long streamDataID;

    @NotNull(message = "streamID cannot be null!")
    @Min(value = 0, message = "streamID cannot be below zero...")
    private Long streamID;

    @Column(columnDefinition = "TEXT")
    @NotNull(message = "Data cannot be null!")
    private String data;

    @CreationTimestamp
    private Instant timestamp;

    public RTStreamDataModel() {
    }

    public RTStreamDataModel(Long streamID, String data) {
        this.streamID = streamID;
        this.data = data;
    }

    public Long getStreamDataID() {
        return streamDataID;
    }

    public Long getStreamID() {
        return streamID;
    }

    public String getData() {
        return data;
    }
}
