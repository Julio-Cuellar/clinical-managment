package com.jc.clinical_managment.common.security.validator;

import com.jc.clinical_managment.common.security.filter.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("userAccessValidator")
@Slf4j
public class UserAccessValidator {
    
    public boolean canAccess(Authentication authentication, String targetUserId) {
        if (authentication == null || targetUserId == null) {
            return false;
        }
        
        Object details = authentication.getDetails();
        if (!(details instanceof UserContext)) {
            log.warn("Authentication details is not UserContext: {}", details);
            return false;
        }
        
        UserContext userContext = (UserContext) details;
        boolean canAccess = userContext.canAccessUser(targetUserId);
        
        log.debug("User {} access to user {}: {}", 
            userContext.getUsername(), targetUserId, canAccess);
        
        return canAccess;
    }
    
    public boolean hasRole(Authentication authentication, String role) {
        if (authentication == null) {
            return false;
        }
        
        Object details = authentication.getDetails();
        if (!(details instanceof UserContext)) {
            return false;
        }
        
        UserContext userContext = (UserContext) details;
        return userContext.hasRole(role);
    }
}