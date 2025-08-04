package com.jc.clinical_managment.consultorio_service.security;

import com.jc.clinical_managment.consultorio_service.domain.Consultorio;
import com.jc.clinical_managment.consultorio_service.repository.ConsultorioRepository;
import com.jc.clinical_managment.common.security.filter.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("consultorioAccessValidator")
@RequiredArgsConstructor
@Slf4j
public class ConsultorioAccessValidator {
    
    private final ConsultorioRepository consultorioRepository;
    
    public boolean canAccessConsultorio(Authentication authentication, Long consultorioId) {
        if (authentication == null || consultorioId == null) {
            return false;
        }
        
        Object details = authentication.getDetails();
        if (!(details instanceof UserContext)) {
            log.warn("Authentication details is not UserContext: {}", details);
            return false;
        }
        
        UserContext userContext = (UserContext) details;
        
        // Admin can access any consultorio
        if (userContext.isAdmin()) {
            return true;
        }
        
        // Check if consultorio belongs to the user
        Optional<Consultorio> consultorio = consultorioRepository.findById(consultorioId);
        if (consultorio.isEmpty()) {
            return false;
        }
        
        boolean canAccess = userContext.getUserId().equals(consultorio.get().getUserId().toString());
        
        log.debug("User {} access to consultorio {}: {}", 
            userContext.getUsername(), consultorioId, canAccess);
        
        return canAccess;
    }
}