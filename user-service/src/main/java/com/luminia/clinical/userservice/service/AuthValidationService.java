package com.luminia.clinical.userservice.service;

import com.luminia.clinical.userservice.client.AuthServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthValidationService {
    
    private final AuthServiceClient authServiceClient;
    
    public boolean validateCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            log.warn("No authentication found in SecurityContext");
            return false;
        }
        
        String username = authentication.getName();
        String token = authentication.getCredentials() != null ? 
                authentication.getCredentials().toString() : null;
        
        if (token == null || token.isEmpty()) {
            log.warn("No token found in authentication");
            return false;
        }
        
        return validateUser(username, token);
    }
    
    public boolean validateUser(String username, String token) {
        try {
            if (!token.startsWith("Bearer ")) {
                token = "Bearer " + token;
            }
            
            ResponseEntity<Boolean> response = authServiceClient.validateUser(username, token);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                Boolean isValid = response.getBody();
                log.debug("Validation result for user {}: {}", username, isValid);
                return Boolean.TRUE.equals(isValid);
            } else {
                log.warn("Auth service returned non-2xx status for user {}: {}", username, response.getStatusCode());
                return false;
            }
        } catch (Exception e) {
            log.error("Error validating user {}: {}", username, e.getMessage(), e);
            return false;
        }
    }
    
    public String[] getUserRoles(String username) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String token = authentication.getCredentials() != null ? 
                    authentication.getCredentials().toString() : null;
            
            if (token == null || token.isEmpty()) {
                log.warn("No token found in authentication for getting user roles");
                return new String[0];
            }
            
            if (!token.startsWith("Bearer ")) {
                token = "Bearer " + token;
            }
            
            ResponseEntity<String[]> response = authServiceClient.getUserRoles(username, token);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                String[] roles = response.getBody();
                log.debug("Retrieved roles for user {}: {}", username, 
                        roles != null ? String.join(", ", roles) : "none");
                return roles != null ? roles : new String[0];
            } else {
                log.warn("Auth service returned non-2xx status for user roles {}: {}", username, response.getStatusCode());
                return new String[0];
            }
        } catch (Exception e) {
            log.error("Error getting user roles for {}: {}", username, e.getMessage(), e);
            return new String[0];
        }
    }
}