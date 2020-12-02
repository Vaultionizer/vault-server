package com.vaultionizer.vaultserver.service;

import com.vaultionizer.vaultserver.helpers.Hashing;
import com.vaultionizer.vaultserver.model.db.UserModel;
import com.vaultionizer.vaultserver.resource.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final HashSet<Long> deletedUsers;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        deletedUsers = new HashSet<>();
    }

    public Long getUserIDCheckCredentials(String username, String key){
        Set<UserModel> users = userRepository.getPwd(username);
        if (users.size() != 1){
            return -1L;
        }
        UserModel model = users.stream().findFirst().get();
        Long userID = Hashing.checkMatchingHash(model.getKey(), key) ? model.getId() : -1;
        return deletedUsers.contains(userID) ? -1L : userID;
    }

    public Long createUser(String username, String key){
        try {return userRepository.save(new UserModel(username, Hashing.hashBcrypt(key))).getId();}
        catch (Exception e){ // in case that username exists
            return null;
        }
    }

    public synchronized boolean setDeleted(Long userID){
        if (deletedUsers.contains(userID)) return false;
        deletedUsers.add(userID);
        return true;
    }

    public synchronized void setDeletionDone(Long userID){
        userRepository.deleteUser(userID);
        deletedUsers.remove(userID);
    }
}
