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
    private final FileService fileService;

    @Autowired
    public PendingUploadService(PendingUploadRepository pendingUploadRepository, FileService fileService) {
        this.pendingUploadRepository = pendingUploadRepository;
        this.fileService = fileService;
    }

    public void addFilesToUpload(Long spaceID, Long sessionID, Long amountValues, Long saveIndex) {
        PendingUploadModel model;
        for (long i = 0; i < amountValues; i++) {
            model = new PendingUploadModel(spaceID, saveIndex + i, sessionID, false);
            this.pendingUploadRepository.save(model);
        }
    }

    // returns 0 if not granted, 1 if usual upload and 2 if update
    public int uploadFile(Long spaceID, Long sessionID, Long saveIndex) {
        var model = pendingUploadRepository.findItem(spaceID, sessionID, saveIndex);
        if (model != null) {
            pendingUploadRepository.delete(model);
            if (!model.getUpdate()) return 1;
            else return 2;
        }
        return 0;
    }

    public boolean updateFile(Long spaceID, Long sessionID, Long saveIndex) {
        if (!fileService.fileExists(spaceID, saveIndex)) return false;
        if (pendingUploadRepository.isPending(spaceID, saveIndex) > 0) return false;
        PendingUploadModel model;
        model = new PendingUploadModel(spaceID, saveIndex, sessionID, true);
        this.pendingUploadRepository.save(model);
        return true;
    }

    public void deleteAllPendingUploads(Long spaceID) {
        pendingUploadRepository.deletePendingUploads(spaceID);
    }

    public void deletePendingUploadsByUser(Long userID) {
        pendingUploadRepository.deleteAllByUser(userID);
    }

    public void deleteOldPendingUploads() {
        pendingUploadRepository.deleteOldUploads(Instant.now().minusSeconds(Config.MAX_UPLOAD_AGE));
    }

    public long countPendingUploadsForSpace(Long spaceID) {
        return pendingUploadRepository.countBySpace(spaceID);
    }
}
