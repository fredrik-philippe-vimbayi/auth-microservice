package com.example.authservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import static com.example.authservice.security.SecurityConstants.EXPIRATION_TIME;
import static com.example.authservice.security.SecurityConstants.PUBLIC_KEY;

public class JwtTokenUtil {

    public static String generateToken(UserDetails user) {
        Date expiration = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        Key key = getSecretKey();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiration)
                .claim("roles", user.getAuthorities().stream().toList())
                .signWith(key)
                .compact();
    }

    private static SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(PUBLIC_KEY.getBytes());
    }

    public static String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }


    private static Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private static <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private static Claims getAllClaimsFromToken(String token) {
        Key key = getSecretKey();
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }


    private static boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public static boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
