package com.vaultionizer.vaultserver.controllers;

import com.vaultionizer.vaultserver.model.UserModel;
import com.vaultionizer.vaultserver.resource.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/user/all")
    Iterable<UserModel> all(){
        return this.userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    UserModel userById(@PathVariable Long id){
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND
        ));
    }

    @PostMapping("/user/save")
    UserModel save(@RequestBody UserModel user){
        return this.userRepository.save(user);
    }
}
