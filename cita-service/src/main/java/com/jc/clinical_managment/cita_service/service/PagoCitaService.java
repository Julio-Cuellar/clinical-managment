package com.jc.clinical_managment.cita_service.service;

import com.jc.clinical_managment.cita_service.dto.PagoCitaRequestDTO;
import com.jc.clinical_managment.cita_service.dto.PagoCitaResponseDTO;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PagoCitaService {
    PagoCitaResponseDTO registrarPago(PagoCitaRequestDTO dto);
    List<PagoCitaResponseDTO> obtenerPagosPorCitaId(Long citaId);
    double obtenerTotalPagadoPorCita(Long citaId);

    // Métodos adicionales
    Optional<PagoCitaResponseDTO> obtenerPagoPorId(Long id);
    List<PagoCitaResponseDTO> listarTodosLosPagos();
    List<PagoCitaResponseDTO> listarPagosPorFecha(Date fecha);
    List<PagoCitaResponseDTO> listarPagosPorRangoFechas(Date inicio, Date fin);
    List<PagoCitaResponseDTO> listarPagosPorMetodo(String metodoPago);
    void eliminarPago(Long id);
    PagoCitaResponseDTO actualizarPago(Long id, PagoCitaRequestDTO dto);
}