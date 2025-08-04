package com.jc.clinical_managment.cita_service.controller;

import com.jc.clinical_managment.cita_service.dto.CitaRequestDTO;
import com.jc.clinical_managment.cita_service.dto.CitaResponseDTO;
import com.jc.clinical_managment.cita_service.service.CitaService;
import com.jc.clinical_managment.common.security.filter.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('PATIENT')")
    public ResponseEntity<CitaResponseDTO> crearCita(@RequestBody CitaRequestDTO dto, Authentication auth) {
        UserContext userContext = (UserContext) auth.getDetails();
        
        // Patients can only create citas for themselves
        if (userContext.isPatient() && !userContext.getUserId().equals(dto.getPacienteId().toString())) {
            dto.setPacienteId(Long.valueOf(userContext.getUserId()));
        }
        
        return ResponseEntity.ok(citaService.crearCita(dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@citaAccessValidator.canAccessCita(authentication, #id)")
    public ResponseEntity<CitaResponseDTO> obtenerCitaPorId(@PathVariable Long id) {
        return citaService.obtenerCitaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public List<CitaResponseDTO> listarCitas(Authentication auth) {
        UserContext userContext = (UserContext) auth.getDetails();
        
        // Doctors can only see their own citas
        if (userContext.isDoctor()) {
            return citaService.listarCitasPorDoctorId(Long.valueOf(userContext.getUserId()));
        }
        
        return citaService.listarCitas();
    }

    @PutMapping("/{id}")
    @PreAuthorize("@citaAccessValidator.canAccessCita(authentication, #id)")
    public ResponseEntity<CitaResponseDTO> actualizarCita(@PathVariable Long id, @RequestBody CitaRequestDTO dto) {
        return ResponseEntity.ok(citaService.actualizarCita(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@citaAccessValidator.canAccessCita(authentication, #id)")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        citaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints adicionales with security
    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("@userAccessValidator.canAccess(authentication, #pacienteId.toString()) or hasRole('ADMIN')")
    public List<CitaResponseDTO> listarCitasPorPacienteId(@PathVariable Long pacienteId) {
        return citaService.listarCitasPorPacienteId(pacienteId);
    }

    @GetMapping("/agenda/{agendaId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public List<CitaResponseDTO> listarCitasPorAgendaId(@PathVariable Long agendaId, Authentication auth) {
        UserContext userContext = (UserContext) auth.getDetails();
        
        // Additional validation: check if doctor owns the agenda (would need agenda service call)
        return citaService.listarCitasPorAgendaId(agendaId);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public List<CitaResponseDTO> listarCitasPorStatus(@PathVariable String status) {
        return citaService.listarCitasPorStatus(status);
    }

    @GetMapping("/fecha")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public List<CitaResponseDTO> listarCitasPorFecha(@RequestParam("fecha") Date fecha) {
        return citaService.listarCitasPorFecha(fecha);
    }

    @GetMapping("/rango")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public List<CitaResponseDTO> listarCitasPorRangoFechas(
            @RequestParam("inicio") Date inicio,
            @RequestParam("fin") Date fin) {
        return citaService.listarCitasPorRangoFechas(inicio, fin);
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public List<CitaResponseDTO> buscarCitasPorMotivo(@RequestParam("motivo") String motivo) {
        return citaService.buscarCitasPorMotivo(motivo);
    }
}