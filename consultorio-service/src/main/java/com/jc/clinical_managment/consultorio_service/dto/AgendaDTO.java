package com.jc.clinical_managment.consultorio_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgendaDTO {
    private Long id;
    private Long usuarioId;
    private String nombre;
    private String descripcion;
}