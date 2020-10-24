package com.vaultionizer.vaultserver.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String key;

    public UserModel() {
    }

    public UserModel(String key) {
        this.key = key;
    }

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }
}