package com.example.authservice.exceptions;

import org.springframework.validation.BindingResult;

public class UsernameAlreadyExistsException extends RuntimeException{

    private final transient BindingResult errors;

    public UsernameAlreadyExistsException(String message, BindingResult errors) {
        super(message);
        this.errors = errors;
    }
}
