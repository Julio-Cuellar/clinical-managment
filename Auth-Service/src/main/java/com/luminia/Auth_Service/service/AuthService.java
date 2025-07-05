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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ClinicRepository clinicRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public User registerNewUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está en uso");
        }

        // Crear clínica
        Clinic clinic = Clinic.builder().name(request.getClinicName()).build();
        clinicRepository.save(clinic);

        // Obtener rol ADMIN
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("No existe el rol ADMIN en la base de datos"));

        // Crear usuario
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .clinic(clinic)
                .roles(Set.of(adminRole))
                .enabled(true)
                .build();

        userRepository.save(user);

        // Asignar owner a la clínica
        clinic.setOwner(user);
        clinicRepository.save(clinic);

        return user;
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario o contraseña incorrectos"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        Set<String> roleNames = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        return jwtUtil.generateToken(user.getUsername(), roleNames);
    }
}