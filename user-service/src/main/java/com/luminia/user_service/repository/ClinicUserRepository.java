package com.luminia.user_service.repository;


import com.luminia.user_service.model.ClinicUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClinicUserRepository extends JpaRepository<ClinicUser, Long> {
    List<ClinicUser> findByClinicId(Long clinicId);
    Optional<ClinicUser> findByEmail(String email); // Para verificar unicidad del email interno
    Optional<ClinicUser> findByAuthServiceUserId(Long authServiceUserId);
}