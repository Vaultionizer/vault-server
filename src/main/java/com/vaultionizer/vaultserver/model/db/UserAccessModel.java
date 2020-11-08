package com.vaultionizer.vaultserver.model.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class UserAccessModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: add unique constraint
    @NotNull(message = "User ID cannot be null!")
    private Long userID;

    @NotNull(message = "Space ID cannot be null!")
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
