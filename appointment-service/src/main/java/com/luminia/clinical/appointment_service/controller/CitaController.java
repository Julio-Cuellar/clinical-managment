package com.luminia.clinical.appointment_service.controller;


import com.luminia.clinical.appointment_service.model.Cita;
import com.luminia.clinical.appointment_service.model.EstadoCita;
import com.luminia.clinical.appointment_service.service.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @PostMapping
    public ResponseEntity<Cita> crearCita(@RequestBody Cita cita) {
        return ResponseEntity.ok(citaService.crearCita(cita));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> actualizarEstado(@PathVariable UUID id, @RequestBody EstadoCita nuevoEstado) {
        citaService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/pago")
    public ResponseEntity<Void> registrarPago(@PathVariable UUID id, @RequestBody BigDecimal montoPagado) {
        citaService.registrarPago(id, montoPagado);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/medico/{id}")
    public ResponseEntity<List<Cita>> listarPorMedico(@PathVariable String id) {
        return ResponseEntity.ok(citaService.obtenerCitasPorMedico(id));
    }
}
