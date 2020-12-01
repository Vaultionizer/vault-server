package com.vaultionizer.vaultserver.model.db;

import com.vaultionizer.vaultserver.helpers.Config;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "username cannot be null")
    @Length(min = 4, message = "Username must at least be 4 characters long!")
    @Column(unique = true)
    private String username;

    @NotNull(message = "Key cannot be null!")
    @Length(min = Config.MIN_USER_KEY_LENGTH, message = "Hashed key must be at least "+Config.MIN_USER_KEY_LENGTH+" characters long!")
    private String key; // Note: key can be blank because a cryptographic key is randomly distributed.
                        //       The length must have a minimum size.

    public UserModel() {
    }

    public UserModel(Long id, String username, String key) {
        this.id = id;
        this.username = username;
        this.key = key;
    }

    public UserModel(String username, String key) {
        this.username = username;
        this.key = key;
    }

    public Long getId() {
        return id;
    }
}