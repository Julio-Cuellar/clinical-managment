package com.jc.clinical_managment.common.security.filter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserContext {
    private String username;
    private String userId;
    private String role;
    private String service;
    private boolean isServiceToken;
    
    public boolean hasRole(String role) {
        return this.role != null && this.role.equals(role);
    }
    
    public boolean isAdmin() {
        return hasRole("ADMIN");
    }
    
    public boolean isDoctor() {
        return hasRole("DOCTOR");
    }
    
    public boolean isPatient() {
        return hasRole("PATIENT");
    }
    
    public boolean canAccessUser(String targetUserId) {
        // Admin can access any user
        if (isAdmin()) {
            return true;
        }
        
        // Users can only access their own data
        return this.userId != null && this.userId.equals(targetUserId);
    }
}