package com.vaultionizer.vaultserver.model.db;

import com.vaultionizer.vaultserver.helpers.SessionTokenGen;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.sql.Timestamp;

@Entity
public class SessionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userID;

    @Column(unique = true)
    @NotNull(message = "Session key cannot be null!")
    @NotBlank(message = "Session key cannot be blank!")
    private String sessionKey;

    @PastOrPresent(message = "Last query cannot possibly be in the future!")
    private Timestamp lastQuery;


    public SessionModel() {
    }

    public SessionModel(Long userID) {
        this.userID = userID;
        this.sessionKey = SessionTokenGen.generateToken();
        this.lastQuery = new Timestamp(System.currentTimeMillis());
    }

    public SessionModel(Long userID, String sessionKey) { // for testing purposes
        this.userID = userID;
        this.sessionKey = sessionKey;
        this.lastQuery = new Timestamp(System.currentTimeMillis());
    }

    public Long getUserID() {
        return userID;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void update(){
        this.lastQuery = new Timestamp(System.currentTimeMillis());
    }

    public SessionModel(Long id, Long userID, String sessionKey, Timestamp lastQuery) {
        this.id = id;
        this.userID = userID;
        this.sessionKey = sessionKey;
        this.lastQuery = lastQuery;
    }
}
