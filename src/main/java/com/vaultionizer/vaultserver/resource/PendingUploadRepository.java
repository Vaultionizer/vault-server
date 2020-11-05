package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.PendingUploadModel;
import com.vaultionizer.vaultserver.model.db.SessionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingUploadRepository extends JpaRepository<PendingUploadModel, Long> {

}
