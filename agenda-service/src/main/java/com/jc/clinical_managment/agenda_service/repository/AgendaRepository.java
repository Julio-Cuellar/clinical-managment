package com.jc.clinical_managment.agenda_service.repository;

import com.jc.clinical_managment.agenda_service.domain.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    List<Agenda> findByUsuarioId(Long usuarioId);
}