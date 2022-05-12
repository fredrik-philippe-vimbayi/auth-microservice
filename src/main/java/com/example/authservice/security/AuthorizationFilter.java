package com.example.authservice.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.authservice.security.SecurityConstants.HEADER_NAME;
import static com.example.authservice.security.SecurityConstants.TOKEN_PREFIX;


public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final AuthUserDetailsService authUserDetailsService;

    public AuthorizationFilter(AuthenticationManager authenticationManager,
                               AuthUserDetailsService authUserDetailsService) {
        super(authenticationManager);
        this.authUserDetailsService = authUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(HEADER_NAME);
        String username = "";
        String jwtToken = "";

        if (header == null) {
            logger.warn(HEADER_NAME + " header not found");
        } else if (!header.startsWith(TOKEN_PREFIX)) {
            logger.warn("Token does not start with 'Bearer'");
        } else {
            jwtToken = header.substring(HEADER_NAME.length() + 1);
            try {
                username = JwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.warn("Unable to get JWT token", e);
            } catch (ExpiredJwtException e) {
                logger.warn("This token has expired", e);
            }
        }

        if (tokenIsValidAndUnauthenticated(username, jwtToken)) {
            UserDetails userDetails = authUserDetailsService.loadUserByUsername(username);

            if (JwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

    private boolean tokenIsValidAndUnauthenticated(String username, String jwtToken) {
        return !username.isEmpty() && !jwtToken.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null;
    }
}
