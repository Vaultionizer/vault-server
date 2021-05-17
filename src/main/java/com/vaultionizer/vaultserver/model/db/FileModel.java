package com.vaultionizer.vaultserver.model.db;

import com.vaultionizer.vaultserver.helpers.FileStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class FileModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileID;

    @NotNull(message = "SpaceID cannot be null!")
    @Min(value = 0, message = "SpaceID cannot be below zero...")
    private Long spaceID;

    @NotNull(message = "SaveIndex cannot be null!")
    @Min(value = 0, message = "SaveIndex cannot be below zero...")
    private Long saveIndex;

    @NotNull(message = "Status flag cannot be null!")
    private FileStatus status;
    // Meaning of the status flag:
    // 0 = no operation is done on the file
    // 1 = is being uploaded
    // 2 = is being read from
    // 3 = is being modified

    public FileModel() {
    }

    public FileModel(Long spaceID, Long saveIndex) {
        this.spaceID = spaceID;
        this.saveIndex = saveIndex;
        this.status = FileStatus.UPLOADING; // is being uploaded
    }

    public FileStatus getStatus() {
        return status;
    }


    public void setStatus(FileStatus status) {
        this.status = status;
    }

    public Long getFileID() {
        return fileID;
    }
}