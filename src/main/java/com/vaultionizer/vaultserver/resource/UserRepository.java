package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    @Query("SELECT it FROM users it WHERE it.username = ?1 AND it.key = ?2")
    Set<UserModel> checkCredentials(String username, String key);
}
