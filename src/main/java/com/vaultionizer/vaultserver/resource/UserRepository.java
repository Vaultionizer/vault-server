package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    @Query("SELECT it FROM users it WHERE it.username = ?1")
    Set<UserModel> getPwd(String username);

    @Transactional
    @Modifying
    @Query("DELETE FROM users it WHERE it.id = ?1")
    void deleteUser(Long userID);
}
