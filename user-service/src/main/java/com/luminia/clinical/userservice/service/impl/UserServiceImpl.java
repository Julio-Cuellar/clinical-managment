package com.luminia.clinical.userservice.service.impl;

import com.luminia.clinical.userservice.dto.CreateUserRequest;
import com.luminia.clinical.userservice.dto.UpdateUserRequest;
import com.luminia.clinical.userservice.dto.UserResponse;
import com.luminia.clinical.userservice.model.*;
import com.luminia.clinical.userservice.repository.*;
import com.luminia.clinical.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ClinicRepository clinicRepository;
    private final MedicalOfficeRepository medicalOfficeRepository;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating user with username: {}", request.getUsername());
        
        // Validate unique constraints
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + request.getUsername());
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .enabled(request.isEnabled())
                .build();

        // Set clinic if provided
        if (request.getClinicId() != null) {
            Clinic clinic = clinicRepository.findById(request.getClinicId())
                    .orElseThrow(() -> new IllegalArgumentException("Clinic not found: " + request.getClinicId()));
            user.setClinic(clinic);
        }

        // Set roles if provided
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.getRoleIds()));
            if (roles.size() != request.getRoleIds().size()) {
                throw new IllegalArgumentException("Some roles not found");
            }
            user.setRoles(roles);
        }

        // Set accessible offices if provided
        if (request.getOfficeIds() != null && !request.getOfficeIds().isEmpty()) {
            Set<MedicalOffice> offices = new HashSet<>(medicalOfficeRepository.findAllById(request.getOfficeIds()));
            if (offices.size() != request.getOfficeIds().size()) {
                throw new IllegalArgumentException("Some offices not found");
            }
            user.setAccessibleOffices(offices);
        }

        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());
        
        return mapToUserResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        log.info("Updating user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        // Update fields if provided
        if (request.getEmail() != null) {
            if (!request.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
        }

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        if (request.getEnabled() != null) {
            user.setEnabled(request.getEnabled());
        }

        if (request.getAccountLocked() != null) {
            user.setAccountLocked(request.getAccountLocked());
        }

        // Update clinic if provided
        if (request.getClinicId() != null) {
            Clinic clinic = clinicRepository.findById(request.getClinicId())
                    .orElseThrow(() -> new IllegalArgumentException("Clinic not found: " + request.getClinicId()));
            user.setClinic(clinic);
        }

        // Update roles if provided
        if (request.getRoleIds() != null) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.getRoleIds()));
            if (roles.size() != request.getRoleIds().size()) {
                throw new IllegalArgumentException("Some roles not found");
            }
            user.setRoles(roles);
        }

        // Update accessible offices if provided
        if (request.getOfficeIds() != null) {
            Set<MedicalOffice> offices = new HashSet<>(medicalOfficeRepository.findAllById(request.getOfficeIds()));
            if (offices.size() != request.getOfficeIds().size()) {
                throw new IllegalArgumentException("Some offices not found");
            }
            user.setAccessibleOffices(offices);
        }

        User savedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", savedUser.getId());
        
        return mapToUserResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        return mapToUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        return mapToUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
        return mapToUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::mapToUserResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> searchUsers(String searchTerm, Pageable pageable) {
        return userRepository.findBySearchTerm(searchTerm, pageable)
                .map(this::mapToUserResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByClinic(Long clinicId) {
        return userRepository.findByClinicId(clinicId).stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByRole(String roleName) {
        return userRepository.findByRoleName(roleName).stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByOffice(Long officeId) {
        return userRepository.findByAccessibleOfficeId(officeId).stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found: " + id);
        }
        userRepository.deleteById(id);
        log.info("User deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional
    public void enableUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        user.setEnabled(true);
        userRepository.save(user);
        log.info("User enabled: {}", id);
    }

    @Override
    @Transactional
    public void disableUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        user.setEnabled(false);
        userRepository.save(user);
        log.info("User disabled: {}", id);
    }

    @Override
    @Transactional
    public void lockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        user.setAccountLocked(true);
        userRepository.save(user);
        log.info("User locked: {}", id);
    }

    @Override
    @Transactional
    public void unlockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        user.setAccountLocked(false);
        userRepository.save(user);
        log.info("User unlocked: {}", id);
    }

    @Override
    @Transactional
    public void assignRolesToUser(Long userId, List<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        Set<Role> newRoles = new HashSet<>(roleRepository.findAllById(roleIds));
        if (newRoles.size() != roleIds.size()) {
            throw new IllegalArgumentException("Some roles not found");
        }
        
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().addAll(newRoles);
        userRepository.save(user);
        log.info("Roles assigned to user {}: {}", userId, roleIds);
    }

    @Override
    @Transactional
    public void removeRolesFromUser(Long userId, List<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        if (user.getRoles() != null) {
            user.getRoles().removeIf(role -> roleIds.contains(role.getId()));
            userRepository.save(user);
        }
        log.info("Roles removed from user {}: {}", userId, roleIds);
    }

    @Override
    @Transactional
    public void assignOfficesToUser(Long userId, List<Long> officeIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        Set<MedicalOffice> newOffices = new HashSet<>(medicalOfficeRepository.findAllById(officeIds));
        if (newOffices.size() != officeIds.size()) {
            throw new IllegalArgumentException("Some offices not found");
        }
        
        if (user.getAccessibleOffices() == null) {
            user.setAccessibleOffices(new HashSet<>());
        }
        user.getAccessibleOffices().addAll(newOffices);
        userRepository.save(user);
        log.info("Offices assigned to user {}: {}", userId, officeIds);
    }

    @Override
    @Transactional
    public void removeOfficesFromUser(Long userId, List<Long> officeIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        if (user.getAccessibleOffices() != null) {
            user.getAccessibleOffices().removeIf(office -> officeIds.contains(office.getId()));
            userRepository.save(user);
        }
        log.info("Offices removed from user {}: {}", userId, officeIds);
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse.UserResponseBuilder builder = UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .enabled(user.isEnabled())
                .accountLocked(user.isAccountLocked())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt());

        if (user.getClinic() != null) {
            builder.clinic(UserResponse.ClinicResponse.builder()
                    .id(user.getClinic().getId())
                    .name(user.getClinic().getName())
                    .address(user.getClinic().getAddress())
                    .contactInfo(user.getClinic().getContactInfo())
                    .build());
        }

        if (user.getRoles() != null) {
            Set<UserResponse.RoleResponse> roleResponses = user.getRoles().stream()
                    .map(role -> UserResponse.RoleResponse.builder()
                            .id(role.getId())
                            .name(role.getName())
                            .description(role.getDescription())
                            .build())
                    .collect(Collectors.toSet());
            builder.roles(roleResponses);
        }

        if (user.getAccessibleOffices() != null) {
            Set<UserResponse.MedicalOfficeResponse> officeResponses = user.getAccessibleOffices().stream()
                    .map(office -> UserResponse.MedicalOfficeResponse.builder()
                            .id(office.getId())
                            .name(office.getName())
                            .location(office.getLocation())
                            .description(office.getDescription())
                            .active(office.isActive())
                            .build())
                    .collect(Collectors.toSet());
            builder.accessibleOffices(officeResponses);
        }

        return builder.build();
    }
}