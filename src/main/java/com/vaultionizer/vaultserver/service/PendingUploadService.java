package com.vaultionizer.vaultserver.service;

import com.vaultionizer.vaultserver.model.db.PendingUploadModel;
import com.vaultionizer.vaultserver.resource.PendingUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PendingUploadService {
    private final PendingUploadRepository pendingUploadRepository;

    @Autowired
    public PendingUploadService(PendingUploadRepository pendingUploadRepository) {
        this.pendingUploadRepository = pendingUploadRepository;
    }

    public void addFilesToUpload(Long spaceID, Long sessionID, Long amountValues, Long saveIndex){
        PendingUploadModel model;
        for (long i = 0; i < amountValues; i++) {
            model = new PendingUploadModel(spaceID, saveIndex+i, sessionID);
            this.pendingUploadRepository.save(model);
        }
    }
}
