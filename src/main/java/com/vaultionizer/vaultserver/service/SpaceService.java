package com.vaultionizer.vaultserver.service;

import com.vaultionizer.vaultserver.model.db.RefFilesModel;
import com.vaultionizer.vaultserver.model.db.SpaceModel;
import com.vaultionizer.vaultserver.resource.SessionRepository;
import com.vaultionizer.vaultserver.resource.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpaceService {

    private final SpaceRepository spaceRepository;
    private RefFileService refFileService;
    private UserAccessService userAccessService;


    @Autowired
    public SpaceService(SpaceRepository spaceRepository, RefFileService refFileService, UserAccessService userAccessService) {
        this.spaceRepository = spaceRepository;
        this.refFileService = refFileService;
        this.userAccessService = userAccessService;
    }


    public void addPrivateSpace(Long userID, String refFileContent){
        RefFilesModel refFile = refFileService.addNewRefFile(refFileContent);
        SpaceModel spaceModel = spaceRepository.save(new SpaceModel(userID, refFile.getRefFileId()));
        userAccessService.addUserAccess(spaceModel.getSpaceID(), userID);
    }
}
