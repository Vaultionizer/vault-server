package com.vaultionizer.vaultserver.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class UserAccessModel {
    @Id
    @GeneratedValue
    private Long id;

    // TODO: add unique constraint
    private Long userID;
    private Long spaceID;

    public UserAccessModel() {
    }

    public UserAccessModel(Long userID, Long spaceID) {
        this.userID = userID;
        this.spaceID = spaceID;
    }

    public Long getUserID() {
        return userID;
    }

    public Long getSpaceID() {
        return spaceID;
    }


}
