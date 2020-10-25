package com.vaultionizer.vaultserver.model.db;

import com.vaultionizer.vaultserver.helpers.SessionTokenGen;

import javax.persistence.*;


@Entity
public class SessionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userID;

    @Column(unique = true)
    private String sessionKey;

    public SessionModel() {
    }

    public SessionModel(Long userID) {
        this.userID = userID;
        this.sessionKey = SessionTokenGen.generateToken();
    }

    public Long getUserID() {
        return userID;
    }

    public String getSessionKey() {
        return sessionKey;
    }


}