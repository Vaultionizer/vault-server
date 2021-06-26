package com.vaultionizer.vaultserver;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestHelpers {
    public static String convertToJSON(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
