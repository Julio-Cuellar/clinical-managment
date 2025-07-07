package com.luminia.clinical.appointment_service.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String pacienteNombre;
    private LocalDateTime fecha;
    private String medicoId;

    @Enumerated(EnumType.STRING)
    private EstadoCita estado;

    private BigDecimal montoTotal;
    private BigDecimal montoPagado;
    private String codigoReferencia;
    private String canal;
    private String observaciones;

    // Getters, setters, constructor con Lombok
}
