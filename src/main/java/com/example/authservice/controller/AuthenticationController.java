package com.example.authservice.controller;

import com.example.authservice.dto.Token;
import com.example.authservice.dto.UserDto;
import com.example.authservice.entity.User;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("authentication")
public class AuthenticationController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;


    public AuthenticationController(PasswordEncoder passwordEncoder, UserRepository userRepository,
                                    TokenService tokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<Token> authenticate(@RequestBody UserDto userDto) {
        User user = userRepository.findByUsername(userDto.username());

        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Incorrect login credentials");

        if (passwordEncoder.matches(userDto.password(), user.getPassword())) {
            Token token = tokenService.generateToken(user);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Incorrect login credentials");
        }
    }

}
