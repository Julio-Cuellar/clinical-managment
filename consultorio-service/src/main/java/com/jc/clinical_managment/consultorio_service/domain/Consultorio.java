package com.jc.clinical_managment.consultorio_service.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Set;


public class Consultorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String nombreConsultorio;

    private String address;

    private Set<Long> agendaId;

    private Long almacenId;

    private Long financeId;

    private Long ticketsId;


}
