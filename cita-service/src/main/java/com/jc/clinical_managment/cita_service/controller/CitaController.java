package com.jc.clinical_managment.cita_service.controller;

import com.jc.clinical_managment.cita_service.dto.CitaRequestDTO;
import com.jc.clinical_managment.cita_service.dto.CitaResponseDTO;
import com.jc.clinical_managment.cita_service.service.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @PostMapping
    public ResponseEntity<CitaResponseDTO> crearCita(@RequestBody CitaRequestDTO dto) {
        return ResponseEntity.ok(citaService.crearCita(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> obtenerCitaPorId(@PathVariable Long id) {
        return citaService.obtenerCitaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<CitaResponseDTO> listarCitas() {
        return citaService.listarCitas();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> actualizarCita(@PathVariable Long id, @RequestBody CitaRequestDTO dto) {
        return ResponseEntity.ok(citaService.actualizarCita(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        citaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints adicionales
    @GetMapping("/paciente/{pacienteId}")
    public List<CitaResponseDTO> listarCitasPorPacienteId(@PathVariable Long pacienteId) {
        return citaService.listarCitasPorPacienteId(pacienteId);
    }

    @GetMapping("/agenda/{agendaId}")
    public List<CitaResponseDTO> listarCitasPorAgendaId(@PathVariable Long agendaId) {
        return citaService.listarCitasPorAgendaId(agendaId);
    }

    @GetMapping("/status/{status}")
    public List<CitaResponseDTO> listarCitasPorStatus(@PathVariable String status) {
        return citaService.listarCitasPorStatus(status);
    }

    @GetMapping("/fecha")
    public List<CitaResponseDTO> listarCitasPorFecha(@RequestParam("fecha") Date fecha) {
        return citaService.listarCitasPorFecha(fecha);
    }

    @GetMapping("/rango")
    public List<CitaResponseDTO> listarCitasPorRangoFechas(
            @RequestParam("inicio") Date inicio,
            @RequestParam("fin") Date fin) {
        return citaService.listarCitasPorRangoFechas(inicio, fin);
    }

    @GetMapping("/buscar")
    public List<CitaResponseDTO> buscarCitasPorMotivo(@RequestParam("motivo") String motivo) {
        return citaService.buscarCitasPorMotivo(motivo);
    }
}