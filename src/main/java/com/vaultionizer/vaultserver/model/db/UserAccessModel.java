package com.vaultionizer.vaultserver.model.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USERACCESSMODEL", uniqueConstraints={
        @UniqueConstraint( name = "unique_id_pair",  columnNames ={"userID","spaceID"})
})
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
