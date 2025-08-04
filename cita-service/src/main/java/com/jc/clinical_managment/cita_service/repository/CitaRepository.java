package com.jc.clinical_managment.cita_service.repository;

import com.jc.clinical_managment.cita_service.domain.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByPacienteId(Long pacienteId);
    List<Cita> findByAgendaId(Long agendaId);
    List<Cita> findByIdUsuario(Long idUsuario);
    List<Cita> findByFechaCita(Date fechaCita);
    List<Cita> findByStatus(String status);
    List<Cita> findByFechaCitaBetween(Date start, Date end);
    List<Cita> findByMotivoCitaContainingIgnoreCase(String motivoCita);
}