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
public class PagoCitaResponseDTO {
    private Long id;
    private Long citaId;
    private Double monto;
    private Date fechaPago;
    private String metodoPago;
    private String referencia;
    private String observaciones;


}