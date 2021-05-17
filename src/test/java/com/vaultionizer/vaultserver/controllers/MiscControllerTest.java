package com.vaultionizer.vaultserver.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class MiscControllerTest {

    @Test
    void testGetVersion() {
        ResponseEntity<?> res = (new MiscController()).getVersion();
        Assertions.assertEquals(200, res.getStatusCodeValue());
        Assertions.assertTrue(res.hasBody());
    }
}
