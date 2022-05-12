package com.example.authservice.controller;

import com.example.authservice.entity.User;
import com.example.authservice.exceptions.BadRequestException;
import com.example.authservice.exceptions.NotUniqueEmailException;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("register")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody User user, BindingResult errors) {
        if(userRepository.existsByEmail(user.getEmail()))
            throw new NotUniqueEmailException("Email is already registered", errors);
        else if (errors.hasErrors())
            throw new BadRequestException("Invalid input", errors);

        userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
