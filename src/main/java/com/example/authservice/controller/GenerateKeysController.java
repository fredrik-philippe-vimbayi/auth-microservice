package com.example.authservice.controller;

import com.example.authservice.entity.Keys;
import com.example.authservice.service.GenerateKeysService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@RestController
@RequestMapping("keys")
public class GenerateKeysController {

    private final GenerateKeysService generateKeysService;

    public GenerateKeysController(GenerateKeysService generateKeysService) {
        this.generateKeysService = generateKeysService;
    }

    @GetMapping()
    public ResponseEntity<Keys> getKeyPair() throws Exception {

        PublicKey publicKey = generateKeysService.getRSAKeys().getPublic();
        PrivateKey privateKey = generateKeysService.getRSAKeys().getPrivate();

        Keys key = new Keys(Base64.getEncoder().encodeToString(publicKey.getEncoded()), Base64.getEncoder().encodeToString(privateKey.getEncoded()));

        return new ResponseEntity<>(key, HttpStatus.OK);
    }

}
