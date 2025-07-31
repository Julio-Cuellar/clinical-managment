package com.jc.clinical_managment.cita_service.controller;

import com.jc.clinical_managment.cita_service.dto.PagoCitaRequestDTO;
import com.jc.clinical_managment.cita_service.dto.PagoCitaResponseDTO;
import com.jc.clinical_managment.cita_service.service.PagoCitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/pagos-cita")
@RequiredArgsConstructor
public class PagoCitaController {

    private final PagoCitaService pagoCitaService;

    @PostMapping
    public ResponseEntity<PagoCitaResponseDTO> registrarPago(@RequestBody PagoCitaRequestDTO dto) {
        return ResponseEntity.ok(pagoCitaService.registrarPago(dto));
    }

    @GetMapping("/cita/{citaId}")
    public List<PagoCitaResponseDTO> obtenerPagosPorCitaId(@PathVariable Long citaId) {
        return pagoCitaService.obtenerPagosPorCitaId(citaId);
    }

    @GetMapping("/total/cita/{citaId}")
    public double obtenerTotalPagadoPorCita(@PathVariable Long citaId) {
        return pagoCitaService.obtenerTotalPagadoPorCita(citaId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoCitaResponseDTO> obtenerPagoPorId(@PathVariable Long id) {
        return pagoCitaService.obtenerPagoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<PagoCitaResponseDTO> listarTodosLosPagos() {
        return pagoCitaService.listarTodosLosPagos();
    }

    @GetMapping("/fecha")
    public List<PagoCitaResponseDTO> listarPagosPorFecha(@RequestParam("fecha") Date fecha) {
        return pagoCitaService.listarPagosPorFecha(fecha);
    }

    @GetMapping("/rango")
    public List<PagoCitaResponseDTO> listarPagosPorRangoFechas(
            @RequestParam("inicio") Date inicio,
            @RequestParam("fin") Date fin) {
        return pagoCitaService.listarPagosPorRangoFechas(inicio, fin);
    }

    @GetMapping("/metodo")
    public List<PagoCitaResponseDTO> listarPagosPorMetodo(@RequestParam("metodo") String metodo) {
        return pagoCitaService.listarPagosPorMetodo(metodo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        pagoCitaService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoCitaResponseDTO> actualizarPago(@PathVariable Long id, @RequestBody PagoCitaRequestDTO dto) {
        return ResponseEntity.ok(pagoCitaService.actualizarPago(id, dto));
    }
}