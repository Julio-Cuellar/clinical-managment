package com.luminia.clinical.api_gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/auth")
    public ResponseEntity<String> authServiceFallback() {
        return ResponseEntity.ok("El servicio de autenticación no está disponible por el momento. Intenta más tarde.");
    }
}
