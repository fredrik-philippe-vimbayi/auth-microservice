package com.example.authservice.controller;

import com.example.authservice.dto.Token;
import com.example.authservice.dto.UserDto;
import com.example.authservice.entity.User;
import com.example.authservice.exceptions.UserNotFoundException;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.TokenService;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("authenticate")
public class AuthenticationController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final Logger logger;

    public AuthenticationController(PasswordEncoder passwordEncoder, UserRepository userRepository,
                                    TokenService tokenService, Logger logger) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.logger = logger;
    }

    @PostMapping
    public ResponseEntity<Token> authenticate(@RequestBody UserDto userDto, BindingResult errors) {
        User user = userRepository.findByUsername(userDto.username());

        if (user == null) {
            logger.warn("Unsuccessful login - username not found");
            throw new UserNotFoundException("Incorrect login credentials", errors);
        }
        
        if (passwordEncoder.matches(userDto.password(), user.getPassword())) {
            Token token = tokenService.generateToken(user);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            logger.warn("Unsuccessful login attempt for userId {}", user.getId());
            throw new UserNotFoundException("Incorrect login credentials", errors);
        }
    }

}
