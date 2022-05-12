package com.example.authservice.controller;

import com.example.authservice.dto.UserDto;
import com.example.authservice.service.UserService;
import com.example.authservice.validator.UserValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("register")
public class RegisterUserController {

    private final UserService userService;
    private final UserValidator userValidator;

    public RegisterUserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDto user) {
        if (userValidator.userExists(user.username()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
