package com.luminia.Auth_Service.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        // Use reflection to set private fields for testing
        try {
            var secretField = JwtUtil.class.getDeclaredField("jwtSecret");
            secretField.setAccessible(true);
            secretField.set(jwtUtil, "TestSecretKeyForJWTTokens123456789");
            
            var expirationField = JwtUtil.class.getDeclaredField("jwtExpirationMs");
            expirationField.setAccessible(true);
            expirationField.set(jwtUtil, 86400000L);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set up test", e);
        }
    }

    @Test
    void testGenerateAndValidateToken() {
        // Given
        String username = "testuser";
        Set<String> roles = Set.of("ADMIN", "DOCTOR");
        
        // When
        String token = jwtUtil.generateToken(username, roles);
        
        // Then
        assertNotNull(token);
        assertTrue(jwtUtil.validateJwt(token));
        assertEquals(username, jwtUtil.getUsernameFromJwt(token));
        
        var extractedRoles = jwtUtil.getRolesFromJwt(token);
        assertEquals(2, extractedRoles.size());
        assertTrue(extractedRoles.contains("ADMIN"));
        assertTrue(extractedRoles.contains("DOCTOR"));
    }

    @Test
    void testInvalidToken() {
        // Given
        String invalidToken = "invalid.jwt.token";
        
        // When & Then
        assertFalse(jwtUtil.validateJwt(invalidToken));
    }
    
    @Test
    void testTokenComponents() {
        // Given
        String username = "doctoruser";
        Set<String> roles = Set.of("DOCTOR");
        
        // When
        String token = jwtUtil.generateToken(username, roles);
        
        // Then
        assertTrue(token.contains("."));  // JWT should have dots
        assertEquals(3, token.split("\\.").length);  // JWT should have 3 parts
    }
}