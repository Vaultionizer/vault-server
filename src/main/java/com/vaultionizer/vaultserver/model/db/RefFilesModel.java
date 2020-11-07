package com.vaultionizer.vaultserver.model.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class RefFilesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refFileId;

    // index of the newest file (the file will be stored with this index)
    @NotNull(message = "Save index cannot be null.")
    @Min(value = 0, message = "Save index must be at least 0.")
    private Long saveIndex;

    @NotNull(message = "Reference file's content cannot be null!")
    private String content;

    public RefFilesModel() {
    }

    public RefFilesModel(Long refFileId, String content) {
        this.refFileId = refFileId;
        this.saveIndex = 0L;
        this.content = content;
    }

    public RefFilesModel(String content) {
        this.saveIndex = 0L;
        this.content = content;
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
    }
}
