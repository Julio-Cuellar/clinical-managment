package com.jc.clinical_managment.consultorio_service.service;

import com.jc.clinical_managment.consultorio_service.dto.ConsultorioRequestDTO;
import com.jc.clinical_managment.consultorio_service.dto.ConsultorioResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ConsultorioService {
    ConsultorioResponseDTO crearConsultorio(ConsultorioRequestDTO dto);
    Optional<ConsultorioResponseDTO> obtenerConsultorioPorId(Long id);
    List<ConsultorioResponseDTO> listarConsultorios();
    List<ConsultorioResponseDTO> listarConsultoriosPorUsuario(Long userId);
    ConsultorioResponseDTO actualizarConsultorio(Long id, ConsultorioRequestDTO dto);
    void eliminarConsultorio(Long id);
    void desactivarConsultorio(Long id);
}