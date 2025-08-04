package com.jc.clinical_managment.consultorio_service.service.impl;

import com.jc.clinical_managment.consultorio_service.client.AgendaServiceClient;
import com.jc.clinical_managment.consultorio_service.domain.Consultorio;
import com.jc.clinical_managment.consultorio_service.dto.AgendaDTO;
import com.jc.clinical_managment.consultorio_service.dto.ConsultorioRequestDTO;
import com.jc.clinical_managment.consultorio_service.dto.ConsultorioResponseDTO;
import com.jc.clinical_managment.consultorio_service.repository.ConsultorioRepository;
import com.jc.clinical_managment.consultorio_service.service.ConsultorioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ConsultorioServiceImpl implements ConsultorioService {
    
    private final ConsultorioRepository consultorioRepository;
    private final AgendaServiceClient agendaServiceClient;
    
    @Override
    public ConsultorioResponseDTO crearConsultorio(ConsultorioRequestDTO dto) {
        log.info("Creating consultorio for user: {}", dto.getUserId());
        
        Consultorio consultorio = Consultorio.builder()
                .userId(dto.getUserId())
                .nombreConsultorio(dto.getNombreConsultorio())
                .address(dto.getAddress())
                .activo(true)
                .build();
        
        // Save consultorio first
        consultorio = consultorioRepository.save(consultorio);
        
        // Create default agenda if requested
        if (dto.isCreateDefaultAgenda()) {
            try {
                AgendaDTO agendaDTO = AgendaDTO.builder()
                        .usuarioId(dto.getUserId())
                        .nombre("Agenda - " + dto.getNombreConsultorio())
                        .descripcion("Agenda por defecto para " + dto.getNombreConsultorio())
                        .build();
                
                // This will use the service-to-service authentication
                AgendaDTO createdAgenda = agendaServiceClient.crearAgenda(agendaDTO, "");
                
                // Add agenda ID to consultorio
                consultorio.getAgendaIds().add(createdAgenda.getId());
                consultorio = consultorioRepository.save(consultorio);
                
                log.info("Created default agenda {} for consultorio {}", createdAgenda.getId(), consultorio.getId());
            } catch (Exception e) {
                log.error("Failed to create default agenda for consultorio {}: {}", consultorio.getId(), e.getMessage());
                // Continue without the agenda - don't fail the consultorio creation
            }
        }
        
        return toResponseDTO(consultorio);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<ConsultorioResponseDTO> obtenerConsultorioPorId(Long id) {
        return consultorioRepository.findById(id).map(this::toResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ConsultorioResponseDTO> listarConsultorios() {
        return consultorioRepository.findByActivoTrue().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ConsultorioResponseDTO> listarConsultoriosPorUsuario(Long userId) {
        return consultorioRepository.findByUserId(userId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public ConsultorioResponseDTO actualizarConsultorio(Long id, ConsultorioRequestDTO dto) {
        Consultorio consultorio = consultorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultorio no encontrado"));
        
        consultorio.setNombreConsultorio(dto.getNombreConsultorio());
        consultorio.setAddress(dto.getAddress());
        
        consultorio = consultorioRepository.save(consultorio);
        return toResponseDTO(consultorio);
    }
    
    @Override
    public void eliminarConsultorio(Long id) {
        consultorioRepository.deleteById(id);
    }
    
    @Override
    public void desactivarConsultorio(Long id) {
        Consultorio consultorio = consultorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultorio no encontrado"));
        
        consultorio.setActivo(false);
        consultorioRepository.save(consultorio);
    }
    
    private ConsultorioResponseDTO toResponseDTO(Consultorio consultorio) {
        return ConsultorioResponseDTO.builder()
                .id(consultorio.getId())
                .userId(consultorio.getUserId())
                .nombreConsultorio(consultorio.getNombreConsultorio())
                .address(consultorio.getAddress())
                .agendaIds(consultorio.getAgendaIds())
                .almacenId(consultorio.getAlmacenId())
                .financeId(consultorio.getFinanceId())
                .ticketsId(consultorio.getTicketsId())
                .activo(consultorio.isActivo())
                .build();
    }
}