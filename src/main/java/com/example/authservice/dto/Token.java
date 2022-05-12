package com.example.authservice.dto;

public record Token(String access_token, String token_type, Long expires_in) {
}
