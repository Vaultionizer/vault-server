package com.vaultionizer.vaultserver.model.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class RTStreamModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long streamID;

    @NotNull(message = "SpaceID cannot be null!")
    @Min(value = 0, message = "SpaceID cannot be below zero...")
    private Long spaceID;

    public RTStreamModel() {
    }

    public RTStreamModel(Long spaceID) {
        this.spaceID = spaceID;
    }

    public Long getStreamID() {
        return streamID;
    }

    public Long getSpaceID() {
        return spaceID;
    }
}
