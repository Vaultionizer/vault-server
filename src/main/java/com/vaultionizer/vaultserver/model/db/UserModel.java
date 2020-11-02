package com.vaultionizer.vaultserver.model.db;

import com.vaultionizer.vaultserver.helpers.Config;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull(message = "Key cannot be null!")
    @Length(min = Config.MIN_USER_KEY_LENGTH, message = "Hashed key must be at least 64 characters long!")
    private String key; // Note: key can be blank because a cryptographic key is randomly distributed.
                        //       The length must have a minimum size.

    public UserModel() {
    }

    public UserModel(Long id, String key) { // for testing purposes
        this.id = id;
        this.key = key;
    }

    public UserModel(String key) {
        this.key = key;
    }

    public Long getId() {
        return id;
    }
}