package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    @Query("SELECT COUNT(it) FROM users it WHERE it.id = ?1 AND it.key = ?2")
    Long checkCredentials(Long userID, String key);
}
