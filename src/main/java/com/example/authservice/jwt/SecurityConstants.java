package com.example.authservice.jwt;

import io.github.cdimascio.dotenv.Dotenv;

public class SecurityConstants {

    public static final String SIGN_UP_URL = "/register";
    public static final String PUBLIC_KEY = getPublicKey();
    public static final String HEADER_NAME = "Authorization";
    public static final Long EXPIRATION_TIME = 2 * 60 * 60 * 10L;

    private static String getPublicKey() {
        Dotenv dotenv = Dotenv.configure().load();
        return dotenv.get("PUBLIC_KEY");
    }

}
