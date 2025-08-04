package com.jc.clinical_managment.agenda_service.controller;

import com.jc.clinical_managment.agenda_service.domain.Agenda;
import com.jc.clinical_managment.agenda_service.service.AgendaService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendas")
public class AgendaController {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @PostMapping
    public ResponseEntity<Agenda> crearAgenda(@RequestBody Agenda agenda, HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        // Puedes asociar la agenda al usuario autenticado si lo deseas usando claims.getSubject()
        return ResponseEntity.ok(agendaService.crearAgenda(agenda));
    }

    @GetMapping
    public ResponseEntity<List<Agenda>> listarAgendas(@RequestParam(required = false) Long usuarioId, HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (usuarioId != null) {
            return ResponseEntity.ok(agendaService.listarAgendasPorUsuario(usuarioId));
        }
        return ResponseEntity.ok(agendaService.listarAgendas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agenda> obtenerAgenda(@PathVariable Long id, HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        return agendaService.obtenerAgendaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agenda> modificarAgenda(@PathVariable Long id, @RequestBody Agenda agenda, HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (!hasAdminRole(claims)) {
            return ResponseEntity.status(403).build();
        }
        return agendaService.modificarAgenda(id, agenda)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAgenda(@PathVariable Long id, HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (!hasAdminRole(claims)) {
            return ResponseEntity.status(403).build();
        }
        agendaService.eliminarAgenda(id);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para verificar si el usuario tiene rol ADMIN
    private boolean hasAdminRole(Claims claims) {
        if (claims == null) return false;
        Object rolesObj = claims.get("roles");
        if (rolesObj == null) return false;
        // Si los roles vienen como String (ej: "USER,ADMIN")
        if (rolesObj instanceof String) {
            return ((String) rolesObj).contains("ADMIN");
        }
        // Si los roles vienen como List
        if (rolesObj instanceof List) {
            return ((List<?>) rolesObj).contains("ADMIN");
        }
        // Si los roles vienen como Array
        if (rolesObj.getClass().isArray()) {
            Object[] arr = (Object[]) rolesObj;
            for (Object rol : arr) {
                if ("ADMIN".equals(rol)) return true;
            }
            return false;
        }
        return false;
    }
}