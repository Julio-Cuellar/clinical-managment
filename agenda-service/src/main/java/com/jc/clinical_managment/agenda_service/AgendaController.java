package com.jc.clinical_managment.agenda_service;

import com.jc.clinical_managment.agenda_service.domain.Agenda;
import com.jc.clinical_managment.agenda_service.service.AgendaService;
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
    public ResponseEntity<Agenda> crearAgenda(@RequestBody Agenda agenda) {
        return ResponseEntity.ok(agendaService.crearAgenda(agenda));
    }

    @GetMapping
    public ResponseEntity<List<Agenda>> listarAgendas(@RequestParam(required = false) Long usuarioId) {
        if (usuarioId != null) {
            return ResponseEntity.ok(agendaService.listarAgendasPorUsuario(usuarioId));
        }
        return ResponseEntity.ok(agendaService.listarAgendas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agenda> obtenerAgenda(@PathVariable Long id) {
        return agendaService.obtenerAgendaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAgenda(@PathVariable Long id) {
        agendaService.eliminarAgenda(id);
        return ResponseEntity.noContent().build();
    }
}