package com.example.authservice.validator;

import com.example.authservice.repository.UserRepository;

public class UserValidator {

    private final UserRepository userRepository;


    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }
}
