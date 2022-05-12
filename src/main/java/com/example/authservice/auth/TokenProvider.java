package com.example.authservice.auth;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.authservice.auth.SecurityConstants.PRIVATE_KEY;

@Component
public class TokenProvider implements Serializable {

    @Value("${jwt.token.validity}")
    public long TOKEN_VALIDITY;

    @Value("${jwt.authorities.key}")
    public String AUTHORITIES_KEY;

    public String getUsernameFromToken(String token) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws InvalidKeySpecException, NoSuchAlgorithmException {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Key getSigningKey() throws InvalidKeySpecException, NoSuchAlgorithmException {

        byte[] encodedKey = Base64.getDecoder().decode(PRIVATE_KEY);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedKey);
        PrivateKey privateKeyTest = keyFactory.generatePrivate(privateKeySpec);

        return privateKeyTest;

//        byte[] keyBytes = Decoders.BASE64.decode(PRIVATE_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getAllClaimsFromToken(String token) throws InvalidKeySpecException, NoSuchAlgorithmException {
        Key key = getSigningKey();

        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) throws InvalidKeySpecException, NoSuchAlgorithmException {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    UsernamePasswordAuthenticationToken getAuthenticationToken(final String token, final Authentication existingAuth) throws InvalidKeySpecException, NoSuchAlgorithmException {

        final JwtParser jwtParser = Jwts.parser().setSigningKey(PRIVATE_KEY);

        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

        final Claims claims = claimsJws.getBody();

        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(getUsernameFromToken(token), "", authorities);
    }

}