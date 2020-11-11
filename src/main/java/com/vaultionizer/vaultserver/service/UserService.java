package com.vaultionizer.vaultserver.service;

import com.vaultionizer.vaultserver.model.db.UserModel;
import com.vaultionizer.vaultserver.resource.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long getUserIDCheckCredentials(String username, String key){
        Set<UserModel> models = userRepository.checkCredentials(username, key);
        if (models.size() != 1){
            return -1L;
        }
        return models.stream().findFirst().get().getId();
    }

    public UserModel createUser(String username, String key){
        try {return userRepository.save(new UserModel(username, key));}
        catch (Exception e){ // in case that username exists
            return null;
        }
    }
}
