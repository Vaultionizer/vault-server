package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.SpaceModel;
import com.vaultionizer.vaultserver.model.dto.SpaceAuthKeyResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

public interface SpaceRepository extends JpaRepository<SpaceModel, Long> {
    @Query("SELECT COUNT(it) FROM SpaceModel it WHERE it.spaceID = ?1 AND it.isPrivateSpace = false AND it.authKey = ?2")
    int checkJoinableWithCredentials(Long spaceID, String authKey);

    @Query("SELECT new com.vaultionizer.vaultserver.model.dto.SpaceAuthKeyResponseDto(it.spaceID, it.authKey)" +
            "FROM SpaceModel it WHERE it.spaceID = ?1 AND it.isPrivateSpace = false")
    Optional<SpaceAuthKeyResponseDto> getSpaceAuthKey(Long spaceID);


    @Query("SELECT it.refFileID FROM SpaceModel it WHERE it.spaceID = ?1")
    Optional<Long> getRefFileID(Long spaceID);

    @Query("SELECT it.spaceID FROM SpaceModel it WHERE it.creatorID = ?1")
    Set<Long> getAllOwnedSpaces(Long userID);

    @Query("SELECT COUNT(it) FROM SpaceModel it WHERE it.spaceID = ?1 AND it.creatorID = ?2")
    int checkIsCreator(Long spaceID, Long userID);

    @Transactional
    @Modifying
    @Query("DELETE FROM SpaceModel it WHERE it.spaceID = ?1")
    void deleteSpace(Long spaceID);
}
