package com.luminia.Auth_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de autenticación (login) de un usuario.
 * Contiene las credenciales necesarias para iniciar sesión.
 */
@Data // Genera getters, setters, toString, equals y hashCode de Lombok
@Builder // Permite usar el patrón de diseño Builder para crear instancias
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
public class AuthRequest {

    private String email;    // El correo electrónico del usuario
    private String password; // La contraseña del usuario
}