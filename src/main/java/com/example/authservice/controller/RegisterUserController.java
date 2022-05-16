package com.example.authservice.controller;

import com.example.authservice.dto.NewUser;
import com.example.authservice.dto.UserDto;
import com.example.authservice.exceptions.BadRequestException;
import com.example.authservice.exceptions.UsernameAlreadyExistsException;
import com.example.authservice.exchange.ExchangeHandler;
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
public class RegisterUserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final ExchangeHandler exchangeHandler;

    public RegisterUserController(UserService userService, UserRepository userRepository,
                                  ExchangeHandler exchangeHandler) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.exchangeHandler = exchangeHandler;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserDto user, BindingResult errors) {
        if (userRepository.existsByUsername(user.username()))
            throw new UsernameAlreadyExistsException("Username is already registered", errors);
        else if (errors.hasErrors())
            throw new BadRequestException("Invalid input", errors);

        NewUser createdUser = userService.createUser(user);
        exchangeHandler.publishNewUser(createdUser);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
