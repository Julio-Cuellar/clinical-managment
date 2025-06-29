package com.luminia.Auth_Service.service;

import com.luminia.Auth_Service.entity.User;
import com.luminia.Auth_Service.enums.Role;
import com.luminia.Auth_Service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario en el sistema.
     * Codifica la contraseña antes de guardarla.
     * @param email El correo electrónico del usuario.
     * @param password La contraseña en texto plano del usuario.
     * @param role El rol del usuario.
     * @return El objeto User guardado.
     * @throws RuntimeException Si el correo electrónico ya está registrado.
     */
    public User register(String email, String password, Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("El correo ya está registrado");
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password)) // Codifica la contraseña
                .role(role)
                .build();

        return userRepository.save(user);
    }

    /**
     * Busca un usuario por su dirección de correo electrónico.
     * @param email La dirección de correo electrónico a buscar.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no.
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}