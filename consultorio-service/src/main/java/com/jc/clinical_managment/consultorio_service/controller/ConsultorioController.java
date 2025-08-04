package com.jc.clinical_managment.consultorio_service.controller;

import com.jc.clinical_managment.consultorio_service.dto.ConsultorioRequestDTO;
import com.jc.clinical_managment.consultorio_service.dto.ConsultorioResponseDTO;
import com.jc.clinical_managment.consultorio_service.service.ConsultorioService;
import com.jc.clinical_managment.common.security.filter.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultorios")
@RequiredArgsConstructor
public class ConsultorioController {
    
    private final ConsultorioService consultorioService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<ConsultorioResponseDTO> crearConsultorio(
            @RequestBody ConsultorioRequestDTO dto, 
            Authentication auth) {
        UserContext userContext = (UserContext) auth.getDetails();
        
        // Non-admin users can only create consultorios for themselves
        if (!userContext.isAdmin() && !userContext.getUserId().equals(dto.getUserId().toString())) {
            dto.setUserId(Long.valueOf(userContext.getUserId()));
        }
        
        return ResponseEntity.ok(consultorioService.crearConsultorio(dto));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("@consultorioAccessValidator.canAccessConsultorio(authentication, #id)")
    public ResponseEntity<ConsultorioResponseDTO> obtenerConsultorio(@PathVariable Long id) {
        return consultorioService.obtenerConsultorioPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<ConsultorioResponseDTO>> listarConsultorios(
            @RequestParam(required = false) Long userId,
            Authentication auth) {
        UserContext userContext = (UserContext) auth.getDetails();
        
        if (userId != null) {
            // Check if user can access the requested user's consultorios
            if (!userContext.isAdmin() && !userContext.getUserId().equals(userId.toString())) {
                return ResponseEntity.status(403).build();
            }
            return ResponseEntity.ok(consultorioService.listarConsultoriosPorUsuario(userId));
        }
        
        // Non-admin users can only see their own consultorios
        if (!userContext.isAdmin()) {
            return ResponseEntity.ok(consultorioService.listarConsultoriosPorUsuario(Long.valueOf(userContext.getUserId())));
        }
        
        return ResponseEntity.ok(consultorioService.listarConsultorios());
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("@consultorioAccessValidator.canAccessConsultorio(authentication, #id)")
    public ResponseEntity<ConsultorioResponseDTO> actualizarConsultorio(
            @PathVariable Long id, 
            @RequestBody ConsultorioRequestDTO dto) {
        return ResponseEntity.ok(consultorioService.actualizarConsultorio(id, dto));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("@consultorioAccessValidator.canAccessConsultorio(authentication, #id)")
    public ResponseEntity<Void> eliminarConsultorio(@PathVariable Long id) {
        consultorioService.eliminarConsultorio(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("@consultorioAccessValidator.canAccessConsultorio(authentication, #id)")
    public ResponseEntity<Void> desactivarConsultorio(@PathVariable Long id) {
        consultorioService.desactivarConsultorio(id);
        return ResponseEntity.noContent().build();
    }
}