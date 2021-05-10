package com.vaultionizer.vaultserver.resource;

import com.vaultionizer.vaultserver.model.db.RTStreamDataModel;
import com.vaultionizer.vaultserver.model.dto.FetchDataResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

public interface RTStreamDataRepository extends JpaRepository<RTStreamDataModel, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM RTStreamDataModel it " +
            "WHERE it.streamID IN " +
            "(SELECT stream FROM RTStreamModel stream WHERE stream.spaceID = ?1)" +
            "")
    void deleteAllStreamDataWithSpace(Long spaceID);

    @Transactional
    @Modifying
    @Query("DELETE FROM RTStreamDataModel it WHERE it.streamID = ?1")
    void deleteAllStreamDataWithStream(Long streamID);

    @Query("SELECT new com.vaultionizer.vaultserver.model.dto.FetchDataResponseDto(it.timestamp, it.data) " +
            "FROM RTStreamDataModel it WHERE it.streamID = ?1 ORDER BY it.timestamp")
    ArrayList<FetchDataResponseDto> fetchData(Long streamID, Long limit);
}
