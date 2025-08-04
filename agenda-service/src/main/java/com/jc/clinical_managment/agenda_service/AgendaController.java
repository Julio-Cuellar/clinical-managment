package com.jc.clinical_managment.agenda_service;

import com.jc.clinical_managment.agenda_service.domain.Agenda;
import com.jc.clinical_managment.agenda_service.service.AgendaService;
import com.jc.clinical_managment.common.security.filter.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendas")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<Agenda> crearAgenda(@RequestBody Agenda agenda, Authentication auth) {
        UserContext userContext = (UserContext) auth.getDetails();
        
        // If not admin, can only create agendas for themselves
        if (!userContext.isAdmin() && !userContext.getUserId().equals(agenda.getUsuarioId().toString())) {
            agenda.setUsuarioId(Long.valueOf(userContext.getUserId()));
        }
        
        return ResponseEntity.ok(agendaService.crearAgenda(agenda));
    }

    @GetMapping
    public ResponseEntity<List<Agenda>> listarAgendas(
            @RequestParam(required = false) Long usuarioId, 
            Authentication auth) {
        UserContext userContext = (UserContext) auth.getDetails();
        
        if (usuarioId != null) {
            // Check if user can access the requested user's agendas
            if (!userContext.isAdmin() && !userContext.getUserId().equals(usuarioId.toString())) {
                return ResponseEntity.status(403).build();
            }
            return ResponseEntity.ok(agendaService.listarAgendasPorUsuario(usuarioId));
        }
        
        // Non-admin users can only see their own agendas
        if (!userContext.isAdmin()) {
            return ResponseEntity.ok(agendaService.listarAgendasPorUsuario(Long.valueOf(userContext.getUserId())));
        }
        
        return ResponseEntity.ok(agendaService.listarAgendas());
    }

    @GetMapping("/{id}")
    @PreAuthorize("@agendaAccessValidator.canAccessAgenda(authentication, #id)")
    public ResponseEntity<Agenda> obtenerAgenda(@PathVariable Long id) {
        return agendaService.obtenerAgendaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@agendaAccessValidator.canAccessAgenda(authentication, #id)")
    public ResponseEntity<Void> eliminarAgenda(@PathVariable Long id) {
        agendaService.eliminarAgenda(id);
        return ResponseEntity.noContent().build();
    }
}