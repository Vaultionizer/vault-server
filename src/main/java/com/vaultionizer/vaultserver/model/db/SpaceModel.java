package com.vaultionizer.vaultserver.model.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SpaceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spaceID;

    private Long creatorID;
    private Long refFileID;
    private boolean isPrivateSpace;
    private String authKey;

    public SpaceModel() {
    }

    public SpaceModel(Long creatorID, Long refFileID, boolean isPrivateSpace, String authKey) {
        this.creatorID = creatorID;
        this.refFileID = refFileID;
        this.isPrivateSpace = isPrivateSpace;
        this.authKey = authKey;
    }

    public SpaceModel(Long creatorID, Long refFileID) {
        this.creatorID = creatorID;
        this.refFileID = refFileID;
        this.isPrivateSpace = true;
    }

    public Long getSpaceID() {
        return spaceID;
    }

    public Long getCreatorID() {
        return creatorID;
    }

    public Long getRefFileID() {
        return refFileID;
    }

    public boolean isPrivateSpace() {
        return isPrivateSpace;
    }

    public String getAuthKey() {
        return authKey;
    }
}
