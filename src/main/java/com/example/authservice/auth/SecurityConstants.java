package com.example.authservice.auth;

import io.github.cdimascio.dotenv.Dotenv;

public class SecurityConstants {

    public static final String PUBLIC_KEY = getPublicKey();
    public static final String PRIVATE_KEY = getPrivateKey();
    public static final long EXPIRATION_TIME = 900_000; // 15 mins
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/services/controller/user";

    private static String getPublicKey(){
        Dotenv dotenv = Dotenv.configure().load();
        return dotenv.get("PUBLIC_KEY");
    }

    private static String getPrivateKey(){
        Dotenv dotenv = Dotenv.configure().load();
        return dotenv.get("PRIVATE_KEY");
    }
}

