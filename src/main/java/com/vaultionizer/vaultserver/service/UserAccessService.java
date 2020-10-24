package com.vaultionizer.vaultserver.service;

import com.vaultionizer.vaultserver.model.db.UserAccessModel;
import com.vaultionizer.vaultserver.resource.UserAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    }

    public void deleteAllWithUser(Long spaceID){

    }
}
