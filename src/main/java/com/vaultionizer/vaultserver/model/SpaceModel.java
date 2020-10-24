package com.vaultionizer.vaultserver.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SpaceModel {
    @Id
    @GeneratedValue
    private Long spaceID;

    private Long creatorID;
    private Long refFileID;
    private boolean isPrivateSpace;
    private String authKey;

    public SpaceModel() {
    }

    public SpaceModel(Long spaceID, Long creatorID, Long refFileID, boolean isPrivateSpace, String authKey) {
        this.spaceID = spaceID;
        this.creatorID = creatorID;
        this.refFileID = refFileID;
        this.isPrivateSpace = isPrivateSpace;
        this.authKey = authKey;
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
