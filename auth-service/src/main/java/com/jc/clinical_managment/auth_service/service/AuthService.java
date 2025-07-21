package com.jc.clinical_managment.auth_service.service;

import com.jc.clinical_managment.auth_service.domain.User;
import com.jc.clinical_managment.auth_service.dto.RegisterRequest;
import com.jc.clinical_managment.auth_service.dto.UserDTO;
import com.jc.clinical_managment.auth_service.dto.LoginRequest;
import com.jc.clinical_managment.auth_service.dto.LoginResponse;
import com.jc.clinical_managment.auth_service.repository.UserRepository;
import com.jc.clinical_managment.auth_service.security.JwtService;
import com.jc.clinical_managment.auth_service.security.TokenBlacklist;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenBlacklist tokenBlacklist;

    // ---------- CREATE ----------
    public UserDTO register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email ya registrado");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username ya registrado");
        }
        User user = User.builder()
                .name(request.getName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .phoneNumber(request.getPhoneNumber())
                .bornDate(request.getBornDate())
                .hashPassword(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .enabled(false) // Por defecto el usuario no está "logueado"
                .build();

        user = userRepository.save(user);
        return toDto(user);
    }

    // ---------- READ ----------
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return toDto(user);
    }

    // ---------- READ ACTIVOS ----------
    public List<UserDTO> getActiveUsers() {
        return userRepository.findAll().stream()
                .filter(User::isEnabled)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ---------- UPDATE ----------
    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setBornDate(dto.getBornDate());
        user.setRole(dto.getRole());
        // No actualices la contraseña aquí por seguridad, haz endpoint aparte
        user = userRepository.save(user);
        return toDto(user);
    }

    // ---------- DELETE ----------
    public void deleteUser(Long id, String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token vacío.");
        }
        // Limpia el token si viene con "Bearer "
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        } else {
            token = token.trim();
        }

        String username = jwtService.extractUsername(token);
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario autenticado no encontrado"));

        if (!currentUser.getId().equals(id)) {
            throw new SecurityException("No tienes permisos para borrar a este usuario");
        }
        userRepository.deleteById(id);
    }

    // ---------- SOFT DELETE / DESACTIVAR ----------
    public void setUserEnabled(Long id, boolean enabled) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    // ---------- LOGIN ----------
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Usuario/contraseña incorrectos"));
        if (!passwordEncoder.matches(request.getPassword(), user.getHashPassword())) {
            throw new BadCredentialsException("Usuario/contraseña incorrectos");
        }
        // Marcar usuario como "logueado"
        user.setEnabled(true);
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new LoginResponse(token, toDto(user));
    }

    // ---------- LOGOUT ----------
    public void logout(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token vacío.");
        }
        // Si viene con "Bearer " delante, quítalo
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        } else {
            token = token.trim();
        }

        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        // Marcar usuario como "no logueado"
        user.setEnabled(false);
        userRepository.save(user);

        tokenBlacklist.blacklistToken(token);
    }

    // ---------- UTILS ----------
    private UserDTO toDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .bornDate(user.getBornDate())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .consultorioIds(user.getConsultorioIds())
                .build();
    }
}