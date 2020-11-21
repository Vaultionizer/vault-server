package com.vaultionizer.vaultserver.model.db;

import com.vaultionizer.vaultserver.helpers.SessionTokenGen;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.Instant;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "sessionKeyUnique", columnNames = {"userID", "sessionKey", "webSocketToken"})
})
public class SessionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "userID")
    private Long userID;

    @Column(unique = true, name = "sessionKey")
    @NotNull(message = "Session key cannot be null!")
    @NotBlank(message = "Session key cannot be blank!")
    private String sessionKey;


    @Column(unique = true, name = "webSocketToken")
    @NotNull(message = "Session key cannot be null!")
    @NotBlank(message = "Session key cannot be blank!")
    private String webSocketToken;

    @PastOrPresent(message = "Last query cannot possibly be in the future!")
    private Instant lastQuery;


    public SessionModel() {
    }

    public SessionModel(Long userID) {
        this.userID = userID;
        this.sessionKey = SessionTokenGen.generateToken();
        this.webSocketToken = SessionTokenGen.generateToken();
        this.lastQuery = Instant.now();
    }

    public SessionModel(Long userID, String sessionKey) { // for testing purposes
        this.userID = userID;
        this.sessionKey = sessionKey;
        this.webSocketToken = SessionTokenGen.generateToken();
        this.lastQuery = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Long getUserID() {
        return userID;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public String getWebSocketToken() {
        return webSocketToken;
    }

    public void update(){
        this.lastQuery = Instant.now();
    }


    public SessionModel(Long id, Long userID, String sessionKey, String webSocketToken, Instant lastQuery) {
        this.id = id;
        this.userID = userID;
        this.sessionKey = sessionKey;
        this.webSocketToken = webSocketToken;
        this.lastQuery = lastQuery;
    }
}
