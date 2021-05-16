package com.vaultionizer.vaultserver.helpers;

import com.vaultionizer.vaultserver.model.dto.GenericAuthDto;
import com.vaultionizer.vaultserver.service.SessionService;
import com.vaultionizer.vaultserver.service.SpaceService;
import com.vaultionizer.vaultserver.service.UserAccessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AccessCheckerUtil {
    private SessionService sessionService;
    private UserAccessService userAccessService;
    private SpaceService spaceService;

    public AccessCheckerUtil(SessionService sessionService, UserAccessService userAccessService, SpaceService spaceService) {
        this.sessionService = sessionService;
        this.userAccessService = userAccessService;
        this.spaceService = spaceService;
    }

    public HttpStatus checkAccess(GenericAuthDto auth, Long spaceID){
        if (!sessionService.getSession(auth.getUserID(), auth.getSessionKey())){
            return HttpStatus.UNAUTHORIZED;
        }
        if (spaceService.checkDeleted(spaceID) ||
                !userAccessService.userHasAccess(auth.getUserID(), spaceID)){
            return HttpStatus.FORBIDDEN;
        }
        return null;
    }

    // check whether user is logged in, has access and whether user is creator. If so, returns null
    public HttpStatus checkPrivilegeLevel(GenericAuthDto auth, Long spaceID){
        HttpStatus accessStatus = checkAccess(auth, spaceID);
        if (accessStatus != null) return accessStatus;
        if (!spaceService.checkCreator(spaceID, auth.getUserID())){
            return HttpStatus.NOT_ACCEPTABLE;
        }
        return null;
    }

    public HttpStatus checkAuthKeyAccess(GenericAuthDto auth, Long spaceID){
        var status = checkAccess(auth, spaceID);
        if (status != null) return status;
        if (!spaceService.userHasAuthKeyAccess(spaceID, auth.getUserID())) {
            return HttpStatus.NOT_ACCEPTABLE;
        }
        return null;
    }

    public HttpStatus checkWriteAccess(GenericAuthDto auth, Long spaceID){
        var status = checkAccess(auth, spaceID);
        if (status != null) return status;
        if (!spaceService.userHasWriteAccess(spaceID, auth.getUserID()))
            return HttpStatus.NOT_ACCEPTABLE;
        return null;
    }
}
