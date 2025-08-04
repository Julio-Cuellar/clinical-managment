package com.jc.clinical_managment.consultorio_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultorioRequestDTO {
    private Long userId;
    private String nombreConsultorio;
    private String address;
    private boolean createDefaultAgenda = true;
}