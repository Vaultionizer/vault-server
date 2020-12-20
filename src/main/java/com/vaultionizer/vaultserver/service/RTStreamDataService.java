package com.vaultionizer.vaultserver.service;

import com.vaultionizer.vaultserver.helpers.Config;
import com.vaultionizer.vaultserver.model.db.RTStreamDataModel;
import com.vaultionizer.vaultserver.resource.RTStreamDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;

@Service
public class RTStreamDataService {
    private final RTStreamDataRepository rtStreamDataRepository;
    private final HashMap<Long, Instant> pushMap; // needed to keep track of minimum delays between pushes

    @Autowired
    public RTStreamDataService(RTStreamDataRepository rtStreamDataRepository) {
        this.rtStreamDataRepository = rtStreamDataRepository;
        pushMap = new HashMap<>();
    }

    public boolean pushData(Long streamID, String content){
        if (!checkPushable(streamID)) return false;
        RTStreamDataModel streamEntry = new RTStreamDataModel(streamID, content);
        rtStreamDataRepository.save(streamEntry);
        return true;
    }

    private synchronized boolean checkPushable(Long streamID){
        Instant lastPush = pushMap.get(streamID);
        Instant now = Instant.now();
        if (lastPush == null){
            pushMap.put(streamID, now);
            return true;
        }
        if (lastPush.isBefore(now.minusMillis(Config.MIN_PUSH_DELAY))){
            pushMap.put(streamID, now);
            return true;
        }
        return false;
    }

    public void deleteStreamData(Long streamID){
        rtStreamDataRepository.deleteAllStreamDataWithStream(streamID);
    }

    public void fetchData(Long streamID, Long limit){

    }
}
