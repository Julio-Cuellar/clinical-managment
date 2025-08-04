package com.jc.clinical_managment.consultorio_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultorioResponseDTO {
    private Long id;
    private Long userId;
    private String nombreConsultorio;
    private String address;
    private Set<Long> agendaIds;
    private Long almacenId;
    private Long financeId;
    private Long ticketsId;
    private boolean activo;
}