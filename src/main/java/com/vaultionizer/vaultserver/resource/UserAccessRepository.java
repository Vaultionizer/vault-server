package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.UserAccessModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface UserAccessRepository extends JpaRepository<UserAccessModel, Long> {
    @Query("SELECT it.spaceID FROM UserAccessModel it WHERE it.userID = ?1")
    Set<Long> getSpacesAccessible(Long userID);

    @Query("SELECT COUNT(it) FROM UserAccessModel it WHERE it.userID = ?1 AND it.spaceID = ?2")
    Long hasAccess(Long userID, Long spaceID);

    @Transactional
    @Modifying
    @Query("DELETE FROM UserAccessModel it WHERE it.spaceID = ?1")
    void deleteSpace(Long spaceID);

    @Transactional
    @Modifying
    @Query("DELETE FROM UserAccessModel it WHERE it.spaceID = ?1 AND it.userID <> ?2")
    void kickAllUsers(Long spaceID, Long creatorID);

    @Transactional
    @Modifying
    @Query("DELETE FROM UserAccessModel it WHERE it.userID = ?1")
    void removeUser(Long userID);

    @Transactional
    @Modifying
    @Query("DELETE FROM UserAccessModel it WHERE it.userID = ?1 AND it.spaceID = ?2")
    void removeUserFromSpace(Long userID, Long spaceID);
}
