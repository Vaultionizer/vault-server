package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FileRepository extends JpaRepository<FileModel, Long> {
    @Query("SELECT it FROM FileModel it " +
            "WHERE it.spaceID = ?1  AND it.saveIndex = ?2")
    FileModel findFile(Long spaceID, Long saveIndex);
}