package com.jc.clinical_managment.consultorio_service.repository;

import com.jc.clinical_managment.consultorio_service.domain.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultorioRepository extends JpaRepository<Consultorio, Long> {
    List<Consultorio> findByUserId(Long userId);
    List<Consultorio> findByActivoTrue();
}