package com.jc.clinical_managment.consultorio_service.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "consultorios")
public class Consultorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String nombreConsultorio;

    private String address;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "consultorio_agendas", joinColumns = @JoinColumn(name = "consultorio_id"))
    @Column(name = "agenda_id")
    @Builder.Default
    private Set<Long> agendaIds = new HashSet<>();

    private Long almacenId;

    private Long financeId;

    private Long ticketsId;

    private boolean activo = true;
}
