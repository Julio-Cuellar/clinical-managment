package com.luminia.user_service.repository;


import com.luminia.user_service.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    List<Clinic> findByOwnerUserId(Long ownerUserId);
    Optional<Clinic> findByName(String name); // Útil para verificar nombres duplicados
}