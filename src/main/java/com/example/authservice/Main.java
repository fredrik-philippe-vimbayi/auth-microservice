package com.example.authservice;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.*;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.authservice.auth.SecurityConstants.PRIVATE_KEY;

public class Main {

    public static void main(String[] args) {

        System.out.println("generating keys");
        KeyPair rsaKeys = null;

        try {
            rsaKeys = getRSAKeys();
        } catch (Exception e) {
            e.printStackTrace();
        }
        PublicKey publicKey = rsaKeys.getPublic();
        PrivateKey privateKey = rsaKeys.getPrivate();

        System.out.println("generated keys");

        String token = generateToken(privateKey);
        System.out.println("Generated Token:\n" + token);

        System.out.println("private key");
        System.out.println(Base64.getEncoder().encodeToString(rsaKeys.getPrivate().getEncoded()));


        System.out.println("public key");
        System.out.println(Base64.getEncoder().encodeToString(rsaKeys.getPublic().getEncoded()));

        verifyToken(token, publicKey);
    }

    public static String generateToken(PrivateKey privateKey) {
        String token = null;
        try {
            Map<String, Object> claims = new HashMap<>();

            // put your information into claim
            claims.put("id", "xxx");
            claims.put("role", "user");
            claims.put("created", new Date());

            byte[] encodedKey = Base64.getDecoder().decode(PRIVATE_KEY);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            KeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedKey);
            PrivateKey privateKeyTest = keyFactory.generatePrivate(privateKeySpec);

            token = Jwts.builder().
                    setClaims(claims).
                    signWith(SignatureAlgorithm.RS256, privateKeyTest).compact();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    // verify and get claims using public key
    private static void verifyToken(String token, PublicKey publicKey) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
            System.out.println(claims.get("id"));
            System.out.println(claims.get("role"));
        } catch (Exception e) {
            claims = null;
        }
    }

    private static KeyPair getRSAKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }
}