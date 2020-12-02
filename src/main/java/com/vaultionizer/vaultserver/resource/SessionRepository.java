package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.SessionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;

public interface SessionRepository extends JpaRepository<SessionModel, Long> {
    @Query("SELECT it FROM SessionModel it WHERE it.userID = ?1 AND it.sessionKey = ?2 AND it.lastQuery > ?3")
    Set<SessionModel> getSessionModelByKey(Long userID, String sessionKey, Instant now);

    @Query("SELECT COUNT(it) FROM SessionModel it WHERE it.webSocketToken = ?1 OR it.sessionKey = ?2")
    int checkUnique(String webSocketToken, String sessionKey);

    @Query("SELECT COUNT(it) FROM SessionModel it " +
            "WHERE it.userID = ?1 AND it.webSocketToken = ?2 AND it.sessionKey = ?3 AND it.lastQuery > ?4")
    int checkValidWebsocketToken(Long userID, String webSocketToken, String sessionKey, Instant now);

    @Transactional
    @Modifying
    @Query("DELETE FROM SessionModel it WHERE it.userID = ?1")
    void logOutUser(Long userID);

    @Transactional
    @Modifying
    @Query("DELETE FROM SessionModel it WHERE it.userID = ?1 AND it.sessionKey = ?2")
    void deleteSession(Long userID, String sessionKey);

    @Transactional
    @Modifying
    @Query("DELETE FROM SessionModel it WHERE it.lastQuery < ?1")
    void deleteAllOldSessions(Instant now);
}
