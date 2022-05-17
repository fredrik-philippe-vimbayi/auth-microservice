package com.example.authservice.service;

import com.example.authservice.dto.Token;
import com.example.authservice.entity.Role;
import com.example.authservice.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;

import static com.example.authservice.security.SecurityConstants.EXPIRATION_TIME;
import static com.example.authservice.security.SecurityConstants.TOKEN_PREFIX;
@Service
public class TokenService {

    private final Logger logger;
    private final KeyService keyService;

    public TokenService(Logger logger, KeyService keyService) {
        this.logger = logger;
        this.keyService = keyService;
    }

    public Token generateToken(User user) {
        Date expiration = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        PrivateKey key = privateKey();
        List<String> roles = getRoles(user);

        String jwtToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiration)
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.RS256,key)
                .compact();

        return new Token(jwtToken, TOKEN_PREFIX, EXPIRATION_TIME);
    }

    private List<String> getRoles(User user) {
        return user.getRoles().stream().map(Role::getName).toList();
    }

    private PrivateKey privateKey() {
        try {
            return keyService.getPrivateKey();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error("Failed to generate token: {}", e.getMessage());
            throw new DataRetrievalFailureException("Failed to generate token.");
        }
    }
}
