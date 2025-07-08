package com.luminia.clinical.userservice.repository;

import com.luminia.clinical.userservice.model.MedicalOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalOfficeRepository extends JpaRepository<MedicalOffice, Long> {
    
    List<MedicalOffice> findByClinicId(Long clinicId);
    
    List<MedicalOffice> findByClinicIdAndActive(Long clinicId, boolean active);
    
    List<MedicalOffice> findByActive(boolean active);
}