package com.example.authservice.service;

import com.example.authservice.dto.Token;
import com.example.authservice.entity.User;
import io.jsonwebtoken.Jwts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import static com.example.authservice.security.SecurityConstants.EXPIRATION_TIME;

public class TokenService {

    private static final Logger logger = LogManager.getLogger("AUTH");
    private final KeyService keyService;

    public TokenService(KeyService keyService) {
        this.keyService = keyService;
    }

    public Token generateToken(User user) {
        Date expiration = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        PrivateKey key = privateKey();

        String jwtToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiration)
                .claim("roles", user.getRoles())
                .signWith(key)
                .compact();

        return new Token(jwtToken, "JWT", EXPIRATION_TIME);
    }

    private PrivateKey privateKey() {
        try {
            return keyService.getPrivateKey();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error getting private key, please check logs for more information.");
        }
    }
}
