package com.jc.clinical_managment.common.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class RS256JwtService {
    
    private final KeyPair keyPair;
    private final long expirationTime;
    
    public RS256JwtService(@Value("${security.jwt.expiration:86400000}") long expirationTime) {
        this.keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
        this.expirationTime = expirationTime;
        log.info("RS256 JWT Service initialized with key pair");
    }
    
    public String generateToken(String username, String userId, String role, String service, Map<String, Object> additionalClaims) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("role", role)
                .claim("service", service)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256);
        
        if (additionalClaims != null) {
            additionalClaims.forEach(builder::claim);
        }
        
        return builder.compact();
    }
    
    public String generateServiceToken(String serviceName, String[] permissions) {
        return Jwts.builder()
                .setSubject(serviceName)
                .claim("type", "service")
                .claim("permissions", permissions)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
    }
    
    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(keyPair.getPublic())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new JwtException("Invalid token", e);
        }
    }
    
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }
    
    public String extractUserId(String token) {
        return extractClaims(token).get("userId", String.class);
    }
    
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }
    
    public String extractService(String token) {
        return extractClaims(token).get("service", String.class);
    }
    
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }
    
    public boolean isServiceToken(String token) {
        try {
            Claims claims = extractClaims(token);
            return "service".equals(claims.get("type"));
        } catch (Exception e) {
            return false;
        }
    }
    
    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }
    
    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }
}