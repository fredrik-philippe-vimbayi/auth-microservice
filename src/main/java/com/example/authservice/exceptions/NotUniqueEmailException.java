package com.example.authservice.exceptions;

import org.springframework.validation.BindingResult;

public class NotUniqueEmailException extends RuntimeException{

    private final transient BindingResult errors;

    public NotUniqueEmailException(String message, BindingResult errors) {
        super(message);
        this.errors = errors;
    }
}
