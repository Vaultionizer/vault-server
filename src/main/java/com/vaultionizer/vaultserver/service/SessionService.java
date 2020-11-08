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
        model.update();
        sessionRepository.save(model);
    }

    public SessionModel addSession(Long userID){
        SessionModel session;
        do
        {
            session = new SessionModel(userID);
        } while(sessionRepository.checkUnique(session.getWebSocketToken(), session.getSessionKey()) > 0);
        return sessionRepository.save(session);
    }

    public boolean getSession(Long userID, String sessionKey){
        SessionModel model = getSessionModel(userID, sessionKey);
        return model!=null;
    }

    public Long getSessionID(Long userID, String sessionKey){
        SessionModel model = getSessionModel(userID, sessionKey);
        return model == null ? -1L : model.getId();
    }

    public String getSessionWebsocketToken(Long userID, String sessionKey){
        SessionModel model = getSessionModel(userID, sessionKey);
        return model == null ? null : model.getWebSocketToken();
    }

    private SessionModel getSessionModel(Long userID, String sessionKey){
        Set<SessionModel> sessions = sessionRepository.getSessionModelByKey(userID, sessionKey);
        if(sessions.size() == 1) {
            SessionModel sessionModel = sessions.stream().findFirst().get();
            updateSessionTimeStamp(sessionModel);
            return sessionModel;
        }
        return null;
    }

    public boolean checkValidWebsocketToken(Long userID, String websocketToken, String sessionKey){
        return sessionRepository.checkValidWebsocketToken(userID, websocketToken, sessionKey) == 1;
    }

    public boolean deleteSession(Long userID, String sessionKey){
        Set<SessionModel> model = sessionRepository.getSessionModelByKey(userID, sessionKey);
        if (model == null) return true;
        if (model.size() == 0) return false;
        model.forEach(m -> {sessionRepository.delete(m);});
        return true;
    }
}
