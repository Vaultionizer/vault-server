package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.UserAccessModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface UserAccessRepository extends JpaRepository<UserAccessModel, Long> {
    @Query("SELECT it.spaceID FROM UserAccessModel it WHERE it.userID = ?1")
    Set<Long> getSpacesAccessible(Long userID);
}