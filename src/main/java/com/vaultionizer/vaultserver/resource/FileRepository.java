package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface FileRepository extends JpaRepository<FileModel, Long> {
    @Query("SELECT it FROM FileModel it " +
            "WHERE it.spaceID = ?1  AND it.saveIndex = ?2")
    FileModel findFile(Long spaceID, Long saveIndex);

    @Transactional
    @Modifying
    @Query("DELETE FROM FileModel it WHERE it.spaceID = ?1")
    void deleteFiles(Long spaceID);

    @Query("SELECT it FROM FileModel it WHERE it.spaceID = ?1")
    Set<Long> getAllFiles(Long spaceID);
}