package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.PendingUploadModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PendingUploadRepository extends JpaRepository<PendingUploadModel, Long> {
    @Query("SELECT it FROM PendingUploadModel it " +
            "WHERE it.spaceID = ?1 AND it.permittedSessionID = ?2 AND it.saveIndex = ?3")
    PendingUploadModel findItem(Long spaceID, Long sessionID, Long saveIndex);

    @Query("DELETE FROM PendingUploadModel it WHERE it.spaceID = ?1")
    void deletePendingUploads(Long spaceID);

    @Query("DELETE FROM PendingUploadModel it " +
            "WHERE it.permittedSessionID IN " +
            "       (SELECT sess FROM SessionModel sess WHERE sess.userID = ?1)")
    void deleteAllByUser(Long userID);
}
