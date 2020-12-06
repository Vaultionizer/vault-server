package com.vaultionizer.vaultserver.helpers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Hashing {
    public static String hashBcrypt(String s){
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        return bcrypt.encode(s);
    }

    public static boolean checkMatchingHash(String hashed, String plain){
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        return bcrypt.matches(plain, hashed);
    }
}
