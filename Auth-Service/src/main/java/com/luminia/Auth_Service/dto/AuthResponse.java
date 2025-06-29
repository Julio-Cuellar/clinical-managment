package com.luminia.Auth_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de una solicitud de autenticación (login o registro).
 * Contiene el token JWT y, opcionalmente, un token de refresco.
 */
@Data // Genera getters, setters, toString, equals y hashCode de Lombok
@Builder // Permite usar el patrón de diseño Builder para crear instancias
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
public class AuthResponse {

    private String token;        // El token JWT de acceso
    private String refreshToken; // (Opcional) El token de refresco, si lo implementas
}