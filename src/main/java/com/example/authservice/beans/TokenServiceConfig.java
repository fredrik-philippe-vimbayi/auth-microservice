package com.example.authservice.beans;

import com.example.authservice.service.KeyService;
import com.example.authservice.service.TokenService;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenServiceConfig {

    @Bean
    public TokenService tokenService(Logger logger, KeyService keyService) {
        return new TokenService(logger, keyService);
    }
}
