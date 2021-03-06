package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.RefFilesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;

public interface RefFileRepository extends JpaRepository<RefFilesModel, Long> {
    @Query("SELECT it.content FROM RefFilesModel it WHERE it.refFileId = ?1")
    Set<String> getRefFileContent(Long refFileID);

    @Query("SELECT it FROM RefFilesModel it WHERE it.refFileId = ?1")
    Set<RefFilesModel> getRefFile(Long refFileID);


    @Transactional
    @Query("SELECT COUNT(it) FROM RefFilesModel it WHERE it.refFileId = ?1 AND it.lastUpdatedContent >= ?2")
    int checkNewVersion(Long refFileID, Instant lastFetched);

    @Transactional
    @Modifying
    @Query("DELETE FROM RefFilesModel it WHERE it.refFileId = ?1")
    void deleteRefFile(Long refFileID);
}
