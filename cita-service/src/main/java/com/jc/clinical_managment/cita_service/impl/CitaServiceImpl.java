package com.jc.clinical_managment.cita_service.impl;

import com.jc.clinical_managment.cita_service.domain.Cita;
import com.jc.clinical_managment.cita_service.dto.CitaRequestDTO;
import com.jc.clinical_managment.cita_service.dto.CitaResponseDTO;
import com.jc.clinical_managment.cita_service.repository.CitaRepository;
import com.jc.clinical_managment.cita_service.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CitaServiceImpl implements CitaService {

    @Autowired
    private CitaRepository citaRepository;

    private Cita toEntity(CitaRequestDTO dto) {
        return Cita.builder()
                .pacienteId(dto.getPacienteId())
                .agendaId(dto.getAgendaId())
                .fechaCita(dto.getFechaCita())
                .motivoCita(dto.getMotivoCita())
                .idUsuario(dto.getIdUsuario())
                .costoCita(dto.getCostoCita())
                .notas(dto.getNotas())
                .build();
    }

    private CitaResponseDTO toResponseDTO(Cita cita) {
        return CitaResponseDTO.builder()
                .id(cita.getId())
                .pacienteId(cita.getPacienteId())
                .agendaId(cita.getAgendaId())
                .fechaCita(cita.getFechaCita())
                .motivoCita(cita.getMotivoCita())
                .idUsuario(cita.getIdUsuario())
                .status(cita.getStatus() != null ? cita.getStatus().toString() : null)
                .statusCobro(cita.getStatusCobro() != null ? cita.getStatusCobro().toString() : null)
                .costoCita(cita.getCostoCita())
                .notas(cita.getNotas())
                .fechaCreacion(cita.getFechaCreacion())
                .fechaActualizacion(cita.getFechaActualizacion())
                .build();
    }

    @Override
    public CitaResponseDTO crearCita(CitaRequestDTO dto) {
        Cita cita = toEntity(dto);
        cita.setFechaCreacion(new Date());
        cita.setFechaActualizacion(new Date());
        Cita creada = citaRepository.save(cita);
        return toResponseDTO(creada);
    }

    @Override
    public Optional<CitaResponseDTO> obtenerCitaPorId(Long id) {
        return citaRepository.findById(id).map(this::toResponseDTO);
    }

    @Override
    public List<CitaResponseDTO> listarCitas() {
        return citaRepository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public CitaResponseDTO actualizarCita(Long id, CitaRequestDTO dto) {
        return citaRepository.findById(id).map(existing -> {
            Cita updated = toEntity(dto);
            updated.setId(id);
            updated.setStatus(existing.getStatus());
            updated.setStatusCobro(existing.getStatusCobro());
            updated.setFechaCreacion(existing.getFechaCreacion());
            updated.setFechaActualizacion(new Date());
            Cita saved = citaRepository.save(updated);
            return toResponseDTO(saved);
        }).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }

    @Override
    public void eliminarCita(Long id) {
        citaRepository.deleteById(id);
    }

    @Override
    public List<CitaResponseDTO> listarCitasPorPacienteId(Long pacienteId) {
        return citaRepository.findByPacienteId(pacienteId).stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<CitaResponseDTO> listarCitasPorAgendaId(Long agendaId) {
        return citaRepository.findByAgendaId(agendaId).stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<CitaResponseDTO> listarCitasPorFecha(Date fecha) {
        return citaRepository.findByFechaCita(fecha).stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<CitaResponseDTO> listarCitasPorStatus(String status) {
        return citaRepository.findByStatus(status).stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<CitaResponseDTO> listarCitasPorRangoFechas(Date fechaInicio, Date fechaFin) {
        return citaRepository.findByFechaCitaBetween(fechaInicio, fechaFin).stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<CitaResponseDTO> buscarCitasPorMotivo(String motivo) {
        return citaRepository.findByMotivoCitaContainingIgnoreCase(motivo).stream().map(this::toResponseDTO).collect(Collectors.toList());
    }
}