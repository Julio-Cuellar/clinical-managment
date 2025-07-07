package com.luminia.Auth_Service.controller;

import com.luminia.Auth_Service.dto.LoginRequest;
import com.luminia.Auth_Service.dto.LoginResponse;
import com.luminia.Auth_Service.dto.RegisterRequest;
import com.luminia.Auth_Service.model.User;
import com.luminia.Auth_Service.repository.UserRepository;
import com.luminia.Auth_Service.security.TokenBlacklistService;
import com.luminia.Auth_Service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final TokenBlacklistService tokenBlacklistService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.login(request.getUsername(), request.getPassword());
            User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
            String[] roles = user.getRoles().stream().map(r -> r.getName()).toArray(String[]::new);
            return ResponseEntity.ok(new LoginResponse(token, user.getUsername(), roles));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }



    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            authService.registerNewUser(request);
            return ResponseEntity.ok("Usuario registrado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.blacklist(token);
            return ResponseEntity.ok("Logout exitoso. Token invalidado.");
        }
        return ResponseEntity.badRequest().body("Authorization header inválido");
    }


}





