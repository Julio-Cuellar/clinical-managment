package com.jc.clinical_managment.common.security.jwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RS256JwtService.class)
@TestPropertySource(properties = {"security.jwt.expiration=3600000"})
class RS256JwtServiceTest {

    private RS256JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new RS256JwtService(3600000L); // 1 hour
    }

    @Test
    void testGenerateAndValidateToken() {
        // Given
        String username = "testuser";
        String userId = "123";
        String role = "DOCTOR";
        String service = "test-service";
        Map<String, Object> additionalClaims = new HashMap<>();
        additionalClaims.put("department", "cardiology");

        // When
        String token = jwtService.generateToken(username, userId, role, service, additionalClaims);

        // Then
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token));
        
        assertEquals(username, jwtService.extractUsername(token));
        assertEquals(userId, jwtService.extractUserId(token));
        assertEquals(role, jwtService.extractRole(token));
        assertEquals(service, jwtService.extractService(token));
        
        assertFalse(jwtService.isServiceToken(token));
    }

    @Test
    void testGenerateServiceToken() {
        // Given
        String serviceName = "consultorio-service";
        String[] permissions = {"agenda:create", "agenda:read"};

        // When
        String token = jwtService.generateServiceToken(serviceName, permissions);

        // Then
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token));
        assertTrue(jwtService.isServiceToken(token));
        assertEquals(serviceName, jwtService.extractUsername(token));
    }

    @Test
    void testInvalidToken() {
        // Given
        String invalidToken = "invalid.token.here";

        // When & Then
        assertFalse(jwtService.isTokenValid(invalidToken));
        assertThrows(Exception.class, () -> jwtService.extractUsername(invalidToken));
    }

    @Test
    void testTokenWithoutAdditionalClaims() {
        // Given
        String username = "testuser";
        String userId = "123";
        String role = "PATIENT";
        String service = "test-service";

        // When
        String token = jwtService.generateToken(username, userId, role, service, null);

        // Then
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token));
        assertEquals(username, jwtService.extractUsername(token));
    }
}