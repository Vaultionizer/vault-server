package com.vaultionizer.vaultserver.model.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class RefFilesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refFileId;

    @NotNull(message = "Reference file's content cannot be null!")
    private String content;

    public RefFilesModel() {
    }

    public RefFilesModel(Long refFileId, @NotNull(message = "Reference file's content cannot be null!") String content) {
        this.refFileId = refFileId;
        this.content = content;
    }

    public RefFilesModel(String content) {
        this.content = content;
    }

    public Long getRefFileId() {
        return refFileId;
    }

    public String getContent() {
        return content;
    }
}
