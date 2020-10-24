package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.UserAccessModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccessRepository extends JpaRepository<UserAccessModel, Long> {
}
