package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.SessionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface SessionRepository extends JpaRepository<SessionModel, Long> {
    @Query("SELECT it FROM SessionModel it WHERE it.sessionKey = ?1")
    Set<SessionModel> getSessionModelByKey(String sessionKey);
}
