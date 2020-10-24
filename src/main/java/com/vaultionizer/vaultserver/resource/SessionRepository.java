package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.SessionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<SessionModel, Long> {
}
