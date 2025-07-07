package com.luminia.clinical.appointment_service.repository;

import com.luminia.clinical.appointment_service.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CitaRepository extends JpaRepository<Cita, UUID> {
    List<Cita> findByMedicoId(String medicoId);

}
