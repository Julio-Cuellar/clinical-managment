package com.luminia.Auth_Service.service;

import com.luminia.Auth_Service.config.JwtUtil;
import com.luminia.Auth_Service.dto.RegisterRequest;
import com.luminia.Auth_Service.model.Clinic;
import com.luminia.Auth_Service.model.Role;
import com.luminia.Auth_Service.model.User;
import com.luminia.Auth_Service.repository.ClinicRepository;
import com.luminia.Auth_Service.repository.RoleRepository;
import com.luminia.Auth_Service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ClinicRepository clinicRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public User registerNewUser(RegisterRequest request) {
        log.info("Starting user registration for username: {}", request.getUsername());
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está en uso");
        }

        // Crear clínica
        log.info("Creating clinic: {}", request.getClinicName());
        Clinic clinic = Clinic.builder().name(request.getClinicName()).build();
        clinic = clinicRepository.save(clinic);
        log.info("Clinic created with ID: {}", clinic.getId());

        // Obtener rol ADMIN
        log.info("Fetching ADMIN role from database");
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("No existe el rol ADMIN en la base de datos"));
        log.info("ADMIN role found with ID: {}, Name: {}", adminRole.getId(), adminRole.getName());

        // Crear usuario con roles inicializados correctamente
        log.info("Creating user with ADMIN role");
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .clinic(clinic)
                .enabled(true)
                .build();

        // Explicitly initialize and add roles to ensure proper persistence
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(adminRole);
        
        log.info("User object created. Roles assigned: {}", 
                user.getRoles() != null ? user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()) : "NULL");

        // Save user with roles
        User savedUser = userRepository.save(user);
        log.info("User saved with ID: {}", savedUser.getId());
        
        // Verificar roles después del guardado
        User userFromDb = userRepository.findById(savedUser.getId()).orElse(null);
        if (userFromDb != null && userFromDb.getRoles() != null) {
            log.info("User roles after save: {}", 
                    userFromDb.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        } else {
            log.warn("User roles are NULL after save - this indicates the persistence issue!");
        }

        // Asignar owner a la clínica
        log.info("Assigning user as clinic owner");
        clinic.setOwner(savedUser);
        clinicRepository.save(clinic);
        log.info("User registration completed successfully");

        return savedUser;
    }

    public String login(String username, String password) {
        log.info("Login attempt for username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario o contraseña incorrectos"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        Set<String> roleNames = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        log.info("User {} logged in successfully with roles: {}", username, roleNames);
        return jwtUtil.generateToken(user.getUsername(), roleNames);
    }

    /**
     * Helper method to verify user role assignments for debugging purposes
     */
    public void verifyUserRoles(String username) {
        log.info("Verifying roles for user: {}", username);
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            log.warn("User {} not found", username);
            return;
        }

        log.info("User {} has {} roles assigned", username, 
                user.getRoles() != null ? user.getRoles().size() : 0);
        
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            Set<String> roleNames = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
            log.info("Roles for user {}: {}", username, roleNames);
        } else {
            log.warn("No roles found for user {} - this indicates a problem with role assignment!", username);
        }
    }
}