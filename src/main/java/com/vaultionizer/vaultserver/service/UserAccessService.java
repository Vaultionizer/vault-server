package com.vaultionizer.vaultserver.service;

import com.vaultionizer.vaultserver.model.db.UserAccessModel;
import com.vaultionizer.vaultserver.resource.UserAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserAccessService {
    private final UserAccessRepository userAccessRepository;


    @Autowired
    public UserAccessService(UserAccessRepository userAccessRepository) {
        this.userAccessRepository = userAccessRepository;
    }

    public void addUserAccess(Long spaceID, Long userID){
        userAccessRepository.save(new UserAccessModel(userID, spaceID));
    }

    public void deleteAllWithSpace(Long spaceID){
        userAccessRepository.deleteSpace(spaceID);
    }

    public void deleteAllWithUser(Long userID){
        userAccessRepository.removeUser(userID);
    }

    // returns all spaceIDs a userID is associated with
    public Set<Long> getAllWithUser(Long userID){
        return userAccessRepository.getSpacesAccessible(userID);
    }

    public boolean userHasAccess(Long userID, Long spaceID){
        return userAccessRepository.hasAccess(userID, spaceID) == 1;
    }

    public boolean removeAccess(Long userID, Long spaceID){
        if (this.userAccessRepository.hasAccess(userID, spaceID) == 1){
            this.userAccessRepository.removeUserFromSpace(userID, spaceID);
            return true;
        }
        return false;
    }

    public void kickAll(Long spaceID, Long creatorID){
        userAccessRepository.kickAllUsers(spaceID, creatorID);
    }
}
