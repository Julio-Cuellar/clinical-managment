package com.jc.clinical_managment.auth_service.controller;

import com.jc.clinical_managment.auth_service.dto.RegisterRequest;
import com.jc.clinical_managment.auth_service.dto.LoginRequest;
import com.jc.clinical_managment.auth_service.dto.LoginResponse;
import com.jc.clinical_managment.auth_service.dto.UserDTO;
import com.jc.clinical_managment.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Registro de usuario
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequest request) {
        UserDTO userDTO = authService.register(request);
        return ResponseEntity.ok(userDTO);
    }

    // Login de usuario
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // Logout (stateless, solo informativo)
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        authService.logout(authHeader);
        return ResponseEntity.ok().build();
    }

    // Mostrar un usuario por ID
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = authService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    // Listar todos los usuarios
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = authService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Actualizar usuario
    @PutMapping("/user/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = authService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // Eliminar usuario: ahora requiere el header Authorization
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        authService.deleteUser(id, authHeader);
        return ResponseEntity.ok().build();
    }
}