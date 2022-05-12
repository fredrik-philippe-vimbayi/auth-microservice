package com.example.authservice.beans;

import com.example.authservice.repository.UserRepository;
import com.example.authservice.validator.UserValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorConfig {

    @Bean
    public static UserValidator userValidator(UserRepository userRepository) {
        return new UserValidator(userRepository);
    }

}
