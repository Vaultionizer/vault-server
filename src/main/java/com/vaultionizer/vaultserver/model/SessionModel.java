package com.vaultionizer.vaultserver.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class SessionModel {
    @Id
    @GeneratedValue
    private Long id;

    private Long userID;

    @Column(unique = true)
    private String sessionKey;

    public SessionModel() {
    }

    public SessionModel(Long userID, String sessionKey) {
        this.userID = userID;
        this.sessionKey = sessionKey;
    }

    public Long getUserID() {
        return userID;
    }

    public String getSessionKey() {
        return sessionKey;
    }


}
