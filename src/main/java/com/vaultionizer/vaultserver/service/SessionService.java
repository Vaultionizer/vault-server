package com.vaultionizer.vaultserver.service;

import com.vaultionizer.vaultserver.helpers.Config;
import com.vaultionizer.vaultserver.model.db.SessionModel;
import com.vaultionizer.vaultserver.model.dto.LoginUserResponseDto;
import com.vaultionizer.vaultserver.resource.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
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

    public LoginUserResponseDto addSession(Long userID) {
        SessionModel session = null;
        do
        {
            try {
                session = new SessionModel(userID);
            }
            catch(NoSuchAlgorithmException e) {
                continue;
            }
        } while(session != null || sessionRepository.checkUnique(session.getWebSocketToken(), session.getSessionKey()) > 0);
        session = sessionRepository.save(session);
        return new LoginUserResponseDto(session.getUserID(), session.getSessionKey(), session.getWebSocketToken());
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
        Set<SessionModel> sessions = sessionRepository.getSessionModelByKey(userID, sessionKey, getAllowedAge());
        if(sessions.size() == 1) {
            SessionModel sessionModel = sessions.stream().findFirst().get();
            updateSessionTimeStamp(sessionModel);
            return sessionModel;
        }
        return null;
    }

    public boolean checkValidWebsocketToken(Long userID, String websocketToken, String sessionKey){
        return sessionRepository.checkValidWebsocketToken(userID, websocketToken, sessionKey, getAllowedAge()) == 1;
    }

    public void deleteSession(Long userID, String sessionKey){
        sessionRepository.deleteSession(userID, sessionKey);
    }

    public void deleteAllSessionsWithUser(Long userID){
        sessionRepository.logOutUser(userID);
    }

    private Instant getAllowedAge(){
        return Instant.now().minusSeconds(Config.MAX_SESSION_AGE);
    }

    public void cleanAllSessionsExpired(){
        this.sessionRepository.deleteAllOldSessions(getAllowedAge());
    }
}
