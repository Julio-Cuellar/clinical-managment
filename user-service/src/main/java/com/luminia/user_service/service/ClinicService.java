package com.luminia.user_service.service;


import com.luminia.user_service.dto.ClinicDto;
import com.luminia.user_service.model.Clinic;

import java.util.List;
import java.util.Optional;

public interface ClinicService {
    Clinic createClinic(ClinicDto clinicDto, Long ownerAuthServiceUserId);
    Optional<Clinic> getClinicById(Long clinicId);
    Clinic updateClinic(Long clinicId, ClinicDto clinicDto);
    void deleteClinic(Long clinicId);
    List<Clinic> getClinicsByOwner(Long ownerAuthServiceUserId);
}