package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.PendingUploadModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

public interface PendingUploadRepository extends JpaRepository<PendingUploadModel, Long> {
    @Query("SELECT it FROM PendingUploadModel it " +
            "WHERE it.spaceID = ?1 AND it.permittedSessionID = ?2 AND it.saveIndex = ?3")
    PendingUploadModel findItem(Long spaceID, Long sessionID, Long saveIndex);


    @Query("SELECT COUNT(it) FROM PendingUploadModel it WHERE it.spaceID = ?1")
    long countBySpace(Long spaceID);


    @Query("SELECT COUNT(it) FROM PendingUploadModel it WHERE it.spaceID = ?1 AND it.saveIndex = ?2")
    int isPending(Long spaceID, Long saveIndex);

    @Transactional
    @Modifying
    @Query("DELETE FROM PendingUploadModel it WHERE it.spaceID = ?1")
    void deletePendingUploads(Long spaceID);

    @Transactional
    @Modifying
    @Query("DELETE FROM PendingUploadModel it " +
            "WHERE it.permittedSessionID IN " +
            "       (SELECT sess FROM SessionModel sess WHERE sess.userID = ?1)")
    void deleteAllByUser(Long userID);

    @Transactional
    @Modifying
    @Query("DELETE FROM PendingUploadModel it WHERE it.requested < ?1")
    void deleteOldUploads(Instant now);
}
