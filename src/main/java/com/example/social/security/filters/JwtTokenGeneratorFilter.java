package com.example.social.security.filters;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtTokenGeneratorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Byt this point, request user has already been authenticated
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null){
            SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
            String jwt = Jwts.builder()
                    .setIssuer("Name of the entity that is issuing this token")
                    .setSubject("JWT Token")
                    .claim("username", auth.getName())
                    .claim("authorities", auth.getAuthorities().stream()
                            .map( GrantedAuthority::getAuthority )
                            .collect(Collectors.joining(","))
                    )
                    .setIssuedAt( new Date())
                    .setExpiration( new Date((new Date()).getTime() + 30000000))
                    .signWith( key ).compact();
            response.setHeader( SecurityConstants.JWT_HEADER, jwt );
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // "Should not execute this filter when request endpoint is not /login"
        return !request.getServletPath().equals("/users/login"); // only execute filter when request goes to /login
    }
}
