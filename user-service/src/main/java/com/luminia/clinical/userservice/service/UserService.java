package com.luminia.clinical.userservice.service;

import com.luminia.clinical.userservice.dto.CreateUserRequest;
import com.luminia.clinical.userservice.dto.UpdateUserRequest;
import com.luminia.clinical.userservice.dto.UserResponse;
import com.luminia.clinical.userservice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    
    UserResponse createUser(CreateUserRequest request);
    
    UserResponse updateUser(Long id, UpdateUserRequest request);
    
    UserResponse getUserById(Long id);
    
    UserResponse getUserByUsername(String username);
    
    UserResponse getUserByEmail(String email);
    
    Page<UserResponse> getAllUsers(Pageable pageable);
    
    Page<UserResponse> searchUsers(String searchTerm, Pageable pageable);
    
    List<UserResponse> getUsersByClinic(Long clinicId);
    
    List<UserResponse> getUsersByRole(String roleName);
    
    List<UserResponse> getUsersByOffice(Long officeId);
    
    void deleteUser(Long id);
    
    void enableUser(Long id);
    
    void disableUser(Long id);
    
    void lockUser(Long id);
    
    void unlockUser(Long id);
    
    void assignRolesToUser(Long userId, List<Long> roleIds);
    
    void removeRolesFromUser(Long userId, List<Long> roleIds);
    
    void assignOfficesToUser(Long userId, List<Long> officeIds);
    
    void removeOfficesFromUser(Long userId, List<Long> officeIds);
}