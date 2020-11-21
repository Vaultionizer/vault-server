package com.vaultionizer.vaultserver.jobs;

import com.vaultionizer.vaultserver.helpers.Config;
import com.vaultionizer.vaultserver.service.PendingUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PendingUploadJob {
    private final PendingUploadService pendingUploadService;

    @Autowired
    public PendingUploadJob(PendingUploadService pendingUploadService) {
        this.pendingUploadService = pendingUploadService;
    }

    @Scheduled(fixedDelay = Config.PENDING_UPLOAD_JOB_DELAY)
    public void cleanOldPendingUploads(){
        pendingUploadService.deleteOldPendingUploads();
    }
}
