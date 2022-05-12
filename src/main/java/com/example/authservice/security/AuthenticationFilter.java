package com.example.authservice.security;

import com.example.authservice.dto.Token;
import com.example.authservice.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

import static com.example.authservice.security.SecurityConstants.EXPIRATION_TIME;
import static com.example.authservice.security.SecurityConstants.PUBLIC_KEY;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final AuthUserDetailsService authUserDetailsService;
    private final ObjectMapper objectMapper;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                AuthUserDetailsService authUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.authUserDetailsService = authUserDetailsService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            UserDto user = new ObjectMapper().readValue(req.getInputStream(), UserDto.class);

            return authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            user.username(), user.password(), new ArrayList<>()
                    ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws IOException {
        UserDetails user = authUserDetailsService.loadUserByUsername(((User) auth.getPrincipal()).getUsername());

        String jwtToken = JwtTokenUtil.generateToken(user);

        Token token = new Token(jwtToken, "JWT", EXPIRATION_TIME);

        res.getWriter().write(objectMapper.writeValueAsString(token));
        res.getWriter().flush();
    }


}
