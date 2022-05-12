package com.example.authservice.controller;

import com.example.authservice.dto.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class SecureEndpointController {

    @GetMapping
    public ResponseEntity<Message> getMessage() {
        Message message = new Message("You have reached a secure endpoint.");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
