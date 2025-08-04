package com.jc.clinical_managment.agenda_service.security;

import com.jc.clinical_managment.agenda_service.domain.Agenda;
import com.jc.clinical_managment.agenda_service.repository.AgendaRepository;
import com.jc.clinical_managment.common.security.filter.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("agendaAccessValidator")
@RequiredArgsConstructor
@Slf4j
public class AgendaAccessValidator {
    
    private final AgendaRepository agendaRepository;
    
    public boolean canAccessAgenda(Authentication authentication, Long agendaId) {
        if (authentication == null || agendaId == null) {
            return false;
        }
        
        Object details = authentication.getDetails();
        if (!(details instanceof UserContext)) {
            log.warn("Authentication details is not UserContext: {}", details);
            return false;
        }
        
        UserContext userContext = (UserContext) details;
        
        // Admin can access any agenda
        if (userContext.isAdmin()) {
            return true;
        }
        
        // Check if agenda belongs to the user
        Optional<Agenda> agenda = agendaRepository.findById(agendaId);
        if (agenda.isEmpty()) {
            return false;
        }
        
        boolean canAccess = userContext.getUserId().equals(agenda.get().getUsuarioId().toString());
        
        log.debug("User {} access to agenda {}: {}", 
            userContext.getUsername(), agendaId, canAccess);
        
        return canAccess;
    }
}