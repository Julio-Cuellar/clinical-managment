package com.luminia.Auth_Service.controller;

import com.luminia.Auth_Service.config.JwtUtil;
import com.luminia.Auth_Service.model.User;
import com.luminia.Auth_Service.repository.UserRepository;
import com.luminia.Auth_Service.security.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ValidationController {
    
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final TokenBlacklistService tokenBlacklistService;

    @GetMapping("/validate/{username}")
    public ResponseEntity<Boolean> validateUser(@PathVariable String username, 
                                              @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token from Authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Invalid Authorization header format");
                return ResponseEntity.ok(false);
            }
            
            String token = authHeader.substring(7);
            
            // Check if token is blacklisted
            if (tokenBlacklistService.isBlacklisted(token)) {
                log.warn("Token is blacklisted");
                return ResponseEntity.ok(false);
            }
            
            // Validate JWT token
            if (!jwtUtil.validateJwt(token)) {
                log.warn("Invalid JWT token");
                return ResponseEntity.ok(false);
            }
            
            // Extract username from token
            String tokenUsername = jwtUtil.getUsernameFromJwt(token);
            
            // Verify username matches the path parameter
            if (!username.equals(tokenUsername)) {
                log.warn("Username in token does not match path parameter");
                return ResponseEntity.ok(false);
            }
            
            // Check if user exists in database
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isEmpty() || !user.get().isEnabled()) {
                log.warn("User not found or disabled: {}", username);
                return ResponseEntity.ok(false);
            }
            
            log.debug("User validation successful for: {}", username);
            return ResponseEntity.ok(true);
            
        } catch (Exception e) {
            log.error("Error validating user: {}", username, e);
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/user/{username}/roles")
    public ResponseEntity<String[]> getUserRoles(@PathVariable String username, 
                                                @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token from Authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Invalid Authorization header format");
                return ResponseEntity.badRequest().build();
            }
            
            String token = authHeader.substring(7);
            
            // Check if token is blacklisted
            if (tokenBlacklistService.isBlacklisted(token)) {
                log.warn("Token is blacklisted");
                return ResponseEntity.status(401).build();
            }
            
            // Validate JWT token
            if (!jwtUtil.validateJwt(token)) {
                log.warn("Invalid JWT token");
                return ResponseEntity.status(401).build();
            }
            
            // Extract username from token
            String tokenUsername = jwtUtil.getUsernameFromJwt(token);
            
            // Verify username matches the path parameter
            if (!username.equals(tokenUsername)) {
                log.warn("Username in token does not match path parameter");
                return ResponseEntity.status(403).build();
            }
            
            // Get user from database
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty() || !userOpt.get().isEnabled()) {
                log.warn("User not found or disabled: {}", username);
                return ResponseEntity.notFound().build();
            }
            
            User user = userOpt.get();
            String[] roles = user.getRoles().stream()
                    .map(role -> role.getName())
                    .toArray(String[]::new);
            
            log.debug("Retrieved roles for user {}: {}", username, String.join(", ", roles));
            return ResponseEntity.ok(roles);
            
        } catch (Exception e) {
            log.error("Error getting user roles: {}", username, e);
            return ResponseEntity.status(500).build();
        }
    }
}