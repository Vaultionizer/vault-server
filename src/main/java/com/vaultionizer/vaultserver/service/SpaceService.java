package com.vaultionizer.vaultserver.service;

import com.vaultionizer.vaultserver.model.db.RefFilesModel;
import com.vaultionizer.vaultserver.model.db.SpaceModel;
import com.vaultionizer.vaultserver.model.dto.GetSpacesResponseDto;
import com.vaultionizer.vaultserver.resource.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

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

    public GetSpacesResponseDto getSpace(Long spaceID, Long userID){
        Optional<SpaceModel> model = spaceRepository.findById(spaceID);
        if (model.isEmpty()) return null;
        return new GetSpacesResponseDto(spaceID, model.get().isPrivateSpace(), model.get().getCreatorID().equals(userID));
    }

    public void addPrivateSpace(Long userID, String refFileContent){
        RefFilesModel refFile = refFileService.addNewRefFile(refFileContent);
        SpaceModel spaceModel = spaceRepository.save(new SpaceModel(userID, refFile.getRefFileId()));
        userAccessService.addUserAccess(spaceModel.getSpaceID(), userID);
    }

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

    public boolean checkSpaceCredentials(Long spaceID, String authKey){
        return spaceRepository.checkJoinableWithCredentials(spaceID, authKey) == 1;
    }
}
