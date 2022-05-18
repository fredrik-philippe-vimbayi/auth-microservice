package com.example.authservice.service;

import org.springframework.stereotype.Service;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

@Service
public class GenerateKeysService {

    public KeyPair getRSAKeys() throws Exception{

        KeyPairGenerator keypairGenerator = KeyPairGenerator.getInstance("RSA");
        keypairGenerator.initialize(2048);
        return keypairGenerator.generateKeyPair();
    }
    
}
