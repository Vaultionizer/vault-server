package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.RefFilesModel;
import com.vaultionizer.vaultserver.model.db.SessionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Set;

public interface RefFileRepository extends JpaRepository<RefFilesModel, Long> {
    @Query("SELECT it.content FROM RefFilesModel it WHERE it.refFileId = ?1")
    Set<String> getRefFileContent(Long refFileID);

    @Query("SELECT it FROM RefFilesModel it WHERE it.refFileId = ?1")
    Set<RefFilesModel> getRefFile(Long refFileID);


    @Query("SELECT COUNT(it) FROM RefFilesModel it WHERE it.refFileId = ?1 AND it.lastUpdatedContent >= ?2")
    int checkNewVersion(Long refFileID, Timestamp lastFetched);

    @Query("DELETE FROM RefFilesModel it WHERE it.refFileId = ?1")
    void deleteRefFile(Long refFileID);
}
