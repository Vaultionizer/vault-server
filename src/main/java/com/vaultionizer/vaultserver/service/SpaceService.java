package com.vaultionizer.vaultserver.service;

import com.vaultionizer.vaultserver.model.db.SpaceModel;
import com.vaultionizer.vaultserver.model.dto.GetSpacesResponseDto;
import com.vaultionizer.vaultserver.model.dto.SpaceAuthKeyResponseDto;
import com.vaultionizer.vaultserver.resource.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class SpaceService {

    private final SpaceRepository spaceRepository;
    private RefFileService refFileService;
    private UserAccessService userAccessService;
    private final HashSet<Long> isDeleted;
    private final Object deleteLock;


    @Autowired
    public SpaceService(SpaceRepository spaceRepository, RefFileService refFileService, UserAccessService userAccessService) {
        this.spaceRepository = spaceRepository;
        this.refFileService = refFileService;
        this.userAccessService = userAccessService;
        this.isDeleted = new HashSet<>();
        deleteLock = new Object();
    }

    public GetSpacesResponseDto getSpace(Long spaceID, Long userID){
        Optional<SpaceModel> model = spaceRepository.findById(spaceID);
        if (model.isEmpty()) return null;
        return new GetSpacesResponseDto(spaceID, model.get().isPrivateSpace(), model.get().getCreatorID().equals(userID),
                model.get().getUsersHaveWriteAccess(), model.get().getUsersCanGetAuthKey());
    }

    public Long createSpace(Long userID, String refFileContent, boolean isPrivate, boolean usersWriteAccess,
                            boolean usersCanInvite, String authKey){
        Long refFileID = refFileService.addNewRefFile(refFileContent);
        SpaceModel model = new SpaceModel(userID, refFileID, isPrivate, usersWriteAccess, usersCanInvite, authKey);
        model = spaceRepository.save(model);
        userAccessService.addUserAccess(model.getSpaceID(), userID);
        return model.getSpaceID();
    }

    public Optional<SpaceAuthKeyResponseDto> getSpaceAuthKey(Long spaceID){
        return spaceRepository.getSpaceAuthKey(spaceID);
    }

    // returns the spaces a user has access to
    public ArrayList<GetSpacesResponseDto> getSpacesAccessible(Long userID){
        ArrayList<GetSpacesResponseDto> spaces = new ArrayList<>();
        Set<Long> spaceIDs = userAccessService.getAllWithUser(userID);
        spaceIDs.forEach(spaceID -> {
            var space = getSpace(spaceID, userID);
            if (space != null) {
                spaces.add(space);
            }
        });
        return spaces;
    }

    // checks whether a space's credentials equal the given ones.
    public boolean checkSpaceCredentials(Long spaceID, String authKey){
        return spaceRepository.checkJoinableWithCredentials(spaceID, authKey) == 1;
    }

    public Long getRefFileID(Long spaceID){
        var id = this.spaceRepository.getRefFileID(spaceID);
        if (id.isEmpty()) return -1L;
        return id.get();
    }

    public Set<Long> getAllOwnedSpaces(Long userID){
        return spaceRepository.getAllOwnedSpaces(userID);
    }

    public boolean checkCreator(Long spaceID, Long userID){
        return spaceRepository.checkIsCreator(spaceID, userID) == 1;
    }

    public boolean markSpaceDeleted(Long spaceID){
        synchronized (deleteLock) {
            if (this.isDeleted.contains(spaceID)) {
                return false;
            }

            this.isDeleted.add(spaceID);
        }
        return true;
    }

    public void deleteSpace(Long spaceID){
        spaceRepository.deleteSpace(spaceID);
        synchronized (deleteLock){
            this.isDeleted.remove(spaceID);
        }
    }

    public synchronized boolean checkDeleted(Long spaceID){
        return this.isDeleted.contains(spaceID);
    }

    public boolean userHasWriteAccess(Long spaceID, Long userID){
        return spaceRepository.getUserWriteAccess(spaceID, userID) == 1;
    }
    public boolean userHasAuthKeyAccess(Long spaceID, Long userID){
        return spaceRepository.getUserAuthKeyAccess(spaceID, userID) == 1;
    }

    public void configureSpace(Long spaceID, boolean writeAccess, boolean authKeyAccess) {
        spaceRepository.configureSpace(spaceID, writeAccess, authKeyAccess);
    }

    public void changeSharedState(Long spaceID, Long creatorID, Boolean shared){
        var spaceModel = spaceRepository.findById(spaceID);
        if (spaceModel.isEmpty() || spaceModel.get().isPrivateSpace() == !shared) return;
        SpaceModel model = spaceModel.get();
        model.setPrivateSpace(!shared);
        spaceRepository.save(model);
        if (!shared){
            // shared -> private: remove all accesses
            userAccessService.kickAll(spaceID, creatorID);
        }
    }
}
