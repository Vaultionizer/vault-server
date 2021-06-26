package com.vaultionizer.vaultserver.model.db;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class SpaceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spaceID;

    @NotNull(message = "Creator ID cannot be null!")
    private Long creatorID;

    @NotNull(message = "Reference file ID cannot be null!")
    private Long refFileID;

    @NotNull(message = "Boolean whether it is a private space cannot be null!")
    private boolean isPrivateSpace;

    @NotNull(message = "Boolean whether normal users have write access cannot be null!")
    private boolean usersHaveWriteAccess;

    @NotNull(message = "Boolean whether normal users can obtain auth key cannot be null!")
    private boolean usersCanGetAuthKey;

    private String authKey;

    public SpaceModel() {
    }

    public SpaceModel(Long spaceID, Long creatorID, Long refFileID, boolean isPrivateSpace, boolean usersHaveWriteAccess,
                      boolean usersCanGetAuthKey, String authKey) {
        this.spaceID = spaceID;
        this.creatorID = creatorID;
        this.refFileID = refFileID;
        this.isPrivateSpace = isPrivateSpace;
        this.authKey = authKey;
        this.usersHaveWriteAccess = usersHaveWriteAccess;
        this.usersCanGetAuthKey = usersCanGetAuthKey;
    }

    public SpaceModel(Long creatorID, Long refFileID, boolean isPrivateSpace, boolean usersHaveWriteAccess,
                      boolean usersCanGetAuthKey, String authKey) {
        this.creatorID = creatorID;
        this.refFileID = refFileID;
        this.isPrivateSpace = isPrivateSpace;
        this.authKey = authKey;
        this.usersHaveWriteAccess = usersHaveWriteAccess;
        this.usersCanGetAuthKey = usersCanGetAuthKey;
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

    public boolean getUsersHaveWriteAccess() {
        return usersHaveWriteAccess;
    }

    public boolean getUsersCanGetAuthKey() {
        return usersCanGetAuthKey;
    }

    public void setPrivateSpace(boolean privateSpace) {
        isPrivateSpace = privateSpace;
    }
}
