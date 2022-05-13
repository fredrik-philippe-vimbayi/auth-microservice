package com.example.authservice.security;

import io.github.cdimascio.dotenv.Dotenv;

public class SecurityConstants {

    public static final String PUBLIC_KEY = getPublicKey();
    public static final String HEADER_NAME = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final Long EXPIRATION_TIME = 2 * 60 * 60 * 10L;

    private static String getPublicKey() {
        Dotenv dotenv = Dotenv.configure().load();
        return dotenv.get("PUBLIC_KEY");
    }

}
