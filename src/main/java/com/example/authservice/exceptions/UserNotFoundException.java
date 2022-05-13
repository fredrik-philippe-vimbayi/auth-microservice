package com.example.authservice.exceptions;

import org.springframework.validation.BindingResult;

public class UserNotFoundException extends RuntimeException {

    private final transient BindingResult errors;

    public UserNotFoundException(String message, BindingResult errors) {
        super(message);
        this.errors = errors;
    }
}