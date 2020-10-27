package com.vaultionizer.vaultserver.model.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull(message = "Key cannot be null!")
    @Min(value = 64, message = "Hashed key must be at least 64 characters long!")
    private String key; // Note: key can be blank because a cryptographic key is randomly distributed.
                        //       The length must have a minimum size.

    public UserModel() {
    }

    public UserModel(String key) {
        this.key = key;
    }

    public Long getId() {
        return id;
    }
}