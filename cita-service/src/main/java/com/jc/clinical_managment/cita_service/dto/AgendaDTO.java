package com.jc.clinical_managment.cita_service.dto;

import lombok.Data;

@Data
public class AgendaDTO {
    private Long id;
    private Long usuarioId;
    private String nombre;
    private String descripcion;
}