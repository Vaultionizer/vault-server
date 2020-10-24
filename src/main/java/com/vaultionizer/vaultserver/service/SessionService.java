package com.vaultionizer.vaultserver.service;

import com.vaultionizer.vaultserver.model.db.SessionModel;
import com.vaultionizer.vaultserver.resource.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SessionService {
    private SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    private void updateSessionTimeStamp(SessionModel model){
        // TODO: update timestamp in model and write to db
    }

    public SessionModel addSession(Long userID){
        SessionModel session;
        do
        {
            session = new SessionModel(userID);

        } while(sessionRepository.getSessionModelByKey(session.getSessionKey()).size() > 0);
        System.out.println(session.getSessionKey());
        return sessionRepository.save(session);
    }

    public Long getSession(String sessionKey){
        Set<SessionModel> sessions = sessionRepository.getSessionModelByKey(sessionKey);
        if(sessions.size() == 1) {
            SessionModel sessionModel = sessions.stream().findFirst().get();
            updateSessionTimeStamp(sessionModel);
            return sessionModel.getUserID();
        }
        return null;
    }
}
