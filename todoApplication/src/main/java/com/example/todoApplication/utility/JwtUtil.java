package com.example.todoApplication.utility;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${spring.data.SECRET_KEY}")
    private String SECRET_KEY;

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        Key geneartionKey = getSigningKey();
        System.out.println("geneartionKey: " + geneartionKey);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        Key extractionKey = getSigningKey();
        System.out.println("extractionKey: " + extractionKey);
        System.out.println("Jwt token: " + token);
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            // Log the exception and return null or handle it as needed
            System.err.println("Invalid JWT token: " + e.getMessage());
            return null;
        }
    }

    public boolean isTokenValid(String token, String username) {
        boolean isValid = username.equals(extractUsername(token)) && !isTokenExpired(token);
        System.out.println("isTokenValid: " + isValid);
        return isValid;
    }

    private boolean isTokenExpired(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (JwtException e) {
            // Log the exception and return true or handle it as needed
            System.err.println("Invalid JWT token: " + e.getMessage());
            return true;
        }
    }
}