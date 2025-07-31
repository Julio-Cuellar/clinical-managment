package com.jc.clinical_managment.cita_service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagoCitaRequestDTO {
    private Long citaId;
    private Double monto;
    private String metodoPago;
    private String referencia;
    private String observaciones;


}