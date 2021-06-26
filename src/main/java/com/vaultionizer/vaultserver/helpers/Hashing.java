package com.vaultionizer.vaultserver.helpers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Hashing {
    private Hashing() {
    }

    public static String hashBcrypt(String s) {
        var bcrypt = new BCryptPasswordEncoder();
        return bcrypt.encode(s);
    }

    public static boolean checkMatchingHash(String hashed, String plain) {
        var bcrypt = new BCryptPasswordEncoder();
        return bcrypt.matches(plain, hashed);
    }
}
