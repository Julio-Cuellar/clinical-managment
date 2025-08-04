package com.jc.clinical_managment.cita_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CitaResponseDTO {
    private Long id;
    private Long pacienteId;
    private Long agendaId;
    private Date fechaCita;
    private String motivoCita;
    private Long idUsuario;
    private String status;
    private String statusCobro;
    private double costoCita;
    private String notas;
    private Date fechaCreacion;
    private Date fechaActualizacion;

}