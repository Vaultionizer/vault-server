package com.vaultionizer.vaultserver.jobs;

import com.vaultionizer.vaultserver.helpers.Config;
import com.vaultionizer.vaultserver.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SessionJob {
    private final SessionService sessionService;

    @Autowired
    public SessionJob(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Scheduled(fixedDelay = Config.SESSION_JOB_DELAY)
    public void cleanSessionsExpired(){
        sessionService.cleanAllSessionsExpired();
    }
}
