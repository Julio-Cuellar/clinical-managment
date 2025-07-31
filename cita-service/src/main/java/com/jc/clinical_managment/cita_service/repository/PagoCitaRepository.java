package com.jc.clinical_managment.cita_service.repository;

import com.jc.clinical_managment.cita_service.domain.PagoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PagoCitaRepository extends JpaRepository<PagoCita, Long> {
    List<PagoCita> findByCitaId(Long citaId);
    List<PagoCita> findByMetodoPago(String metodoPago);
    List<PagoCita> findByFechaPago(Date fechaPago);
    List<PagoCita> findByFechaPagoBetween(Date inicio, Date fin);
}