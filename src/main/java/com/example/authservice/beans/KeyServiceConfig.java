package com.example.authservice.beans;

import com.example.authservice.service.KeyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyServiceConfig {

    @Bean
    public KeyService keyService() {
        return new KeyService();
    }
}
