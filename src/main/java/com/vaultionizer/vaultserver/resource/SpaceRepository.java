package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.SessionModel;
import com.vaultionizer.vaultserver.model.db.SpaceModel;
import com.vaultionizer.vaultserver.model.dto.SpaceAuthKeyResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface SpaceRepository extends JpaRepository<SpaceModel, Long> {
    @Query("SELECT COUNT(it) FROM SpaceModel it WHERE it.spaceID = ?1 AND it.isPrivateSpace = false AND it.authKey = ?2")
    int checkJoinableWithCredentials(Long spaceID, String authKey);

    @Query("SELECT new com.vaultionizer.vaultserver.model.dto.SpaceAuthKeyResponseDto(it.spaceID, it.authKey)" +
            "FROM SpaceModel it WHERE it.spaceID = ?1 AND it.isPrivateSpace = false")
    Optional<SpaceAuthKeyResponseDto> getSpaceAuthKey(Long spaceID);

}
