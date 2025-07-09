package com.luminia.clinical.userservice.controller;

import com.luminia.clinical.userservice.service.AuthValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthTestController {
    
    private final AuthValidationService authValidationService;
    
    @GetMapping("/test")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('ASSISTANT')")
    public ResponseEntity<Map<String, Object>> testAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", authentication != null);
        response.put("username", authentication != null ? authentication.getName() : null);
        response.put("authorities", authentication != null ? authentication.getAuthorities() : null);
        
        // Test validation with Auth Service
        boolean isValid = authValidationService.validateCurrentUser();
        response.put("validatedWithAuthService", isValid);
        
        if (authentication != null && authentication.getName() != null) {
            String[] roles = authValidationService.getUserRoles(authentication.getName());
            response.put("rolesFromAuthService", roles);
        }
        
        log.info("Auth test completed for user: {}", authentication != null ? authentication.getName() : "anonymous");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/validate/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> validateUser(@PathVariable String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = authentication.getCredentials() != null ? 
                authentication.getCredentials().toString() : null;
        
        Map<String, Object> response = new HashMap<>();
        
        if (token != null) {
            boolean isValid = authValidationService.validateUser(username, token);
            String[] roles = authValidationService.getUserRoles(username);
            
            response.put("username", username);
            response.put("valid", isValid);
            response.put("roles", roles);
        } else {
            response.put("error", "No token available");
        }
        
        return ResponseEntity.ok(response);
    }
}