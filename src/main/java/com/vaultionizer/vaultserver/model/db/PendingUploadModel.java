package com.vaultionizer.vaultserver.model.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.Instant;

@Entity
public class PendingUploadModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uploadID;

    @NotNull(message = "SpaceID cannot be null!")
    @Min(value = 0, message = "SpaceID cannot be below zero...")
    private Long spaceID;

    @NotNull(message = "SaveIndex cannot be null!")
    @Min(value = 0, message = "SaveIndex cannot be below zero...")
    private Long saveIndex;

    @NotNull(message = "SessionID that is permitted to do the upload cannot be null!")
    @Min(value = 0, message = "PermittedSessionID cannot be below zero...")
    private Long permittedSessionID; // not the token but the id of the session

    @PastOrPresent(message = "The upload cannot possibly have been requested in the future!")
    private Instant requested;

    public PendingUploadModel() {
    }

    public PendingUploadModel(Long spaceID, Long saveIndex, Long permittedSessionID) {
        this.spaceID = spaceID;
        this.saveIndex = saveIndex;
        this.permittedSessionID = permittedSessionID;
        this.requested = Instant.now();
    }

    public Long getUploadID() {
        return uploadID;
    }

    public Long getSpaceID() {
        return spaceID;
    }

    public Long getSaveIndex() {
        return saveIndex;
    }

    public Long getPermittedSessionID() {
        return permittedSessionID;
    }

    public Instant getRequested() {
        return requested;
    }
}
