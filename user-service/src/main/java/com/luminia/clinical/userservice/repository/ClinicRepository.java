package com.luminia.clinical.userservice.repository;

import com.luminia.clinical.userservice.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    
    Optional<Clinic> findByName(String name);
    
    boolean existsByName(String name);
}