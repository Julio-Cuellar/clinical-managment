package com.luminia.Auth_Service.repository;

import com.luminia.Auth_Service.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
}
