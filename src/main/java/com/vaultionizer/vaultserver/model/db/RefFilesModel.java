package com.vaultionizer.vaultserver.model.db;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.Instant;

@Entity
public class RefFilesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refFileId;

    // index of the newest file (the file will be stored with this index)
    @NotNull(message = "Save index cannot be null.")
    @Min(value = 0, message = "Save index must be at least 0.")
    private Long saveIndex;

    @NotNull(message = "Last time updated content cannot be null!")
    @PastOrPresent(message = "Last time updated content cannot possibly be in the future!")
    private Instant lastUpdatedContent; // needed to specify when the ref file has been updated the last time

    @Column(columnDefinition = "TEXT")
    @NotNull(message = "Reference file's content cannot be null!")
    private String content;


    public RefFilesModel() {
    }

    public RefFilesModel(Long refFileId, String content) {
        this.refFileId = refFileId;
        this.saveIndex = 0L;
        this.lastUpdatedContent = Instant.now();
        this.content = content;
    }

    public RefFilesModel(String content) {
        this.saveIndex = 0L;
        this.content = content;
        this.lastUpdatedContent = Instant.now();
    }

    public Long getRefFileId() {
        return refFileId;
    }

    public Long getSaveIndex() {
        return saveIndex;
    }

    public String getContent() {
        return content;
    }

    public void addToSaveIndex(Long offset) {
        this.saveIndex += offset;
    }

    public void setContent(String content) {
        this.content = content;
        this.lastUpdatedContent = Instant.now();
    }
}
