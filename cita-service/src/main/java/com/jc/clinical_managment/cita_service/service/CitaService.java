package com.jc.clinical_managment.cita_service.service;

import com.jc.clinical_managment.cita_service.dto.CitaRequestDTO;
import com.jc.clinical_managment.cita_service.dto.CitaResponseDTO;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CitaService {
    CitaResponseDTO crearCita(CitaRequestDTO dto);
    Optional<CitaResponseDTO> obtenerCitaPorId(Long id);
    List<CitaResponseDTO> listarCitas();
    CitaResponseDTO actualizarCita(Long id, CitaRequestDTO dto);
    void eliminarCita(Long id);

    // Métodos adicionales
    List<CitaResponseDTO> listarCitasPorPacienteId(Long pacienteId);
    List<CitaResponseDTO> listarCitasPorAgendaId(Long agendaId);
    List<CitaResponseDTO> listarCitasPorFecha(Date fecha);
    List<CitaResponseDTO> listarCitasPorStatus(String status);
    List<CitaResponseDTO> listarCitasPorRangoFechas(Date fechaInicio, Date fechaFin);
    List<CitaResponseDTO> buscarCitasPorMotivo(String motivo);
}