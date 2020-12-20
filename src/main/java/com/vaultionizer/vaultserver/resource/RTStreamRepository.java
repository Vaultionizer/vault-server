package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.RTStreamModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface RTStreamRepository extends JpaRepository<RTStreamModel, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM RTStreamModel it WHERE it.spaceID = ?1")
    void deleteAllStreamsForSpace(Long spaceID);


    @Transactional
    @Modifying
    @Query("DELETE FROM RTStreamModel it WHERE it.streamID = ?1")
    void deleteStream(Long streamID);


    @Query("SELECT it.streamID FROM RTStreamModel it WHERE it.spaceID = ?1")
    Set<Long> getAllStreams(Long spaceID);

    @Query("SELECT it.spaceID FROM RTStreamModel it WHERE it.streamID = ?1")
    Set<Long> getSpaceID(Long streamID);


}
