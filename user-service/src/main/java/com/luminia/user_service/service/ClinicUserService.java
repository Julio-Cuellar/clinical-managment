package com.luminia.user_service.service;


import com.luminia.user_service.dto.ClinicUserDto;
import com.luminia.user_service.model.ClinicUser;

import java.util.List;
import java.util.Optional;

public interface ClinicUserService {
    ClinicUser createInternalUser(Long clinicId, ClinicUserDto userDto);
    Optional<ClinicUser> getClinicUserById(Long clinicUserId);
    ClinicUser updateClinicUser(Long clinicUserId, ClinicUserDto userDto);
    void deleteClinicUser(Long clinicUserId);
    List<ClinicUser> getUsersByClinic(Long clinicId);
    ClinicUser assignRoleToClinicUser(Long clinicUserId, String newRoleName);
}