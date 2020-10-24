package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.UserAccessModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccessRepository extends JpaRepository<UserAccessModel, Long> {
}
