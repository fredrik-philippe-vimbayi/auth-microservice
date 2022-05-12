package com.example.authservice.beans;

import com.example.authservice.service.KeyService;
import com.example.authservice.service.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenServiceConfig {

    @Bean
    public TokenService tokenService(KeyService keyService) {
        return new TokenService(keyService);
    }
}
