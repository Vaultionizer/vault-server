package com.vaultionizer.vaultserver.helpers;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class SessionTokenGen {
    public static String generateToken() throws NoSuchAlgorithmException {
        byte[] content = new byte[64];
        SecureRandom.getInstance(Config.randomAlgo).nextBytes(content);
        return Base64.getEncoder().encodeToString(content);
    }

    public static String generateUUID(){
        return UUID.randomUUID().toString();
    }
}
