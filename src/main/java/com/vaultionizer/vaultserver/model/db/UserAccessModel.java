package com.vaultionizer.vaultserver.model.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserAccessModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
