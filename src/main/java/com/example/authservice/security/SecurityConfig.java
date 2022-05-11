package com.example.authservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/register", "/authenticate").permitAll()
                .and()
                .build();
    }
}
