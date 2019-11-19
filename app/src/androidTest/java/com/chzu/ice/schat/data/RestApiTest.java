package com.chzu.ice.schat.data;

import com.chzu.ice.schat.utils.RSAUtil;

import org.junit.Test;

import java.security.KeyPair;

public class RestApiTest {
    @Test
    public void login() {
        KeyPair keyPair = RSAUtil.generateRSAKeyPair(4096);
        assert keyPair != null;
    }
}