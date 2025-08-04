package com.jc.clinical_managment.cita_service.security;

import com.jc.clinical_managment.cita_service.domain.Cita;
import com.jc.clinical_managment.cita_service.repository.CitaRepository;
import com.jc.clinical_managment.common.security.filter.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("citaAccessValidator")
@RequiredArgsConstructor
@Slf4j
public class CitaAccessValidator {
    
    private final CitaRepository citaRepository;
    
    public boolean canAccessCita(Authentication authentication, Long citaId) {
        if (authentication == null || citaId == null) {
            return false;
        }
        
        Object details = authentication.getDetails();
        if (!(details instanceof UserContext)) {
            log.warn("Authentication details is not UserContext: {}", details);
            return false;
        }
        
        UserContext userContext = (UserContext) details;
        
        // Admin can access any cita
        if (userContext.isAdmin()) {
            return true;
        }
        
        // Check if cita belongs to the user (either as patient or doctor)
        Optional<Cita> cita = citaRepository.findById(citaId);
        if (cita.isEmpty()) {
            return false;
        }
        
        // Patient can access their own citas
        if (userContext.isPatient() && userContext.getUserId().equals(cita.get().getPacienteId().toString())) {
            return true;
        }
        
        // Doctor can access citas assigned to them
        if (userContext.isDoctor() && userContext.getUserId().equals(cita.get().getIdUsuario().toString())) {
            return true;
        }
        
        boolean canAccess = false;
        log.debug("User {} access to cita {}: {}", 
            userContext.getUsername(), citaId, canAccess);
        
        return canAccess;
    }
}