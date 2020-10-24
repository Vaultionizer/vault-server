package com.vaultionizer.vaultserver.helpers;

import java.util.UUID;

public class SessionTokenGen {
    public static String generateToken(){
        return UUID.randomUUID().toString();
    }
}
