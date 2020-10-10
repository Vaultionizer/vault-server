package com.vaultionizer.vaultserver.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "users_table")
public class UserModel {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
}