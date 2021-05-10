package com.vaultionizer.vaultserver.service;

import com.vaultionizer.vaultserver.model.db.RTStreamModel;
import com.vaultionizer.vaultserver.resource.RTStreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RTStreamService {
    private final RTStreamRepository rtStreamRepository;
    private final RTStreamDataService rtStreamDataService;


    @Autowired
    public RTStreamService(RTStreamRepository rtStreamRepository, RTStreamDataService rtStreamDataService) {
        this.rtStreamRepository = rtStreamRepository;
        this.rtStreamDataService = rtStreamDataService;
    }


    public Long createStream(Long spaceID){
        RTStreamModel model = new RTStreamModel(spaceID);
        rtStreamRepository.save(model);
        return model.getStreamID();
    }

    public void deleteStreams(Long spaceID){
        // TODO
    }

    public void deleteStream(Long streamID){
        rtStreamDataService.deleteStreamData(streamID);
        rtStreamRepository.deleteStream(streamID);
    }

    public Long getSpaceID(Long streamID){
        var ids = rtStreamRepository.getSpaceID(streamID);
        if (ids.size() != 1) return null;
        return ids.stream().findFirst().get();
    }
}
