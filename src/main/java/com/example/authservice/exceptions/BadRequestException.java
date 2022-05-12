package com.example.authservice.exceptions;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;

public class BadRequestException extends RuntimeException {
    private final transient BindingResult errors;

    public BadRequestException(String message, BindingResult errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getMessages() {
        return getValidationMessage(this.errors);
    }

    @Override
    public String getMessage() {
        return this.getMessages().toString();
    }

    private static List<String> getValidationMessage(BindingResult bindingResult) {
        return bindingResult.getAllErrors()
                .stream()
                .map(BadRequestException::getValidationMessage)
                .toList();
    }

    private static String getValidationMessage(ObjectError error) {
        if (error instanceof FieldError fieldError) {
            String property = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            return String.format("%s %s", property, message);
        }
        return String.format("%s: %s", error.getObjectName(), error.getDefaultMessage());
    }
}
