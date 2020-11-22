package com.vaultionizer.vaultserver.service;

import com.vaultionizer.vaultserver.helpers.Config;
import com.vaultionizer.vaultserver.model.db.PendingUploadModel;
import com.vaultionizer.vaultserver.resource.PendingUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

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

    public boolean uploadFile(Long spaceID, Long sessionID, Long saveIndex){
        var model = pendingUploadRepository.findItem(spaceID, sessionID, saveIndex);
        if (model != null){
            pendingUploadRepository.delete(model);
            return true;
        }
        return false;
    }

    public void deleteAllPendingUploads(Long spaceID){
        pendingUploadRepository.deletePendingUploads(spaceID);
    }

    public void deletePendingUploadsByUser(Long userID){
        pendingUploadRepository.deleteAllByUser(userID);
    }

    public void deleteOldPendingUploads(){
        pendingUploadRepository.deleteOldUploads(Instant.now().minusSeconds(Config.MAX_UPLOAD_AGE));
    }
}
