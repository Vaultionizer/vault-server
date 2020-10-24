package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.RefFilesModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefFileRepository extends JpaRepository<RefFilesModel, Long> {
}
