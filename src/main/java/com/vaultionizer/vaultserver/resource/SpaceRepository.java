package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.SpaceModel;
import com.vaultionizer.vaultserver.model.dto.GetSpaceConfigResponseDto;
import com.vaultionizer.vaultserver.model.dto.GetSpacesResponseDto;
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

    @Query("SELECT COUNT(it) FROM SpaceModel it WHERE it.spaceID = ?1 " +
            "AND (it.creatorID = ?2 OR it.usersHaveWriteAccess = true)")
    int getUserWriteAccess(Long spaceID, Long userID); // user has write access if creator or normal users have write access

    @Query("SELECT COUNT(it) FROM SpaceModel it WHERE it.spaceID = ?1 " +
            "AND (it.creatorID = ?2 OR it.usersCanGetAuthKey = true)")
    int getUserAuthKeyAccess(Long spaceID, Long userID); // user has access to auth key if creator or normal users have access to auth key

    @Transactional
    @Modifying
    @Query("UPDATE SpaceModel it SET it.usersHaveWriteAccess = ?2, it.usersCanGetAuthKey = ?3 WHERE it.spaceID = ?1")
    void configureSpace(Long spaceID, boolean writeAccess, boolean authKeyAccess);

    @Query("SELECT new com.vaultionizer.vaultserver.model.dto.GetSpaceConfigResponseDto(it.isPrivateSpace, it.usersHaveWriteAccess, it.usersCanGetAuthKey) " +
            "FROM SpaceModel it WHERE it.spaceID = ?1")
    GetSpaceConfigResponseDto getSpaceConfig(Long spaceID);


    @Transactional
    @Modifying
    @Query("UPDATE SpaceModel it SET it.authKey = ?2 WHERE it.spaceID = ?1")
    void updateAuthKey(Long spaceID, String authKey);
}
