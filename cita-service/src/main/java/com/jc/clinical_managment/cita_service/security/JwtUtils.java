package com.jc.clinical_managment.cita_service.security;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms:86400000}") // 1 día por defecto
    private long jwtExpirationMs;

    public String generateToken(String username, Long userId, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            // Puedes loggear el error si lo deseas
        }
        return false;
    }

    public Claims getClaimsFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromJwtToken(String token) {
        return getClaimsFromJwtToken(token).getSubject();
    }

    public Long getUserIdFromJwtToken(String token) {
        Object userId = getClaimsFromJwtToken(token).get("userId");
        if (userId instanceof Integer) return ((Integer) userId).longValue();
        if (userId instanceof Long) return (Long) userId;
        if (userId instanceof String) return Long.parseLong((String) userId);
        return null;
    }

    public String getRoleFromJwtToken(String token) {
        Object role = getClaimsFromJwtToken(token).get("role");
        return role != null ? role.toString() : null;
    }
}