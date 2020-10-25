package com.vaultionizer.vaultserver.model.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RefFilesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refFileId;

    private String content;

    public RefFilesModel() {
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
