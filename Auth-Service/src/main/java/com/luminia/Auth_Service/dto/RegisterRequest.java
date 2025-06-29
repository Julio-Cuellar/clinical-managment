package com.luminia.Auth_Service.dto;

import com.luminia.Auth_Service.enums.Role; // Importa tu enum Role
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de registro de un nuevo usuario.
 * Contiene los datos necesarios para crear una nueva cuenta.
 */
@Data // Genera getters, setters, toString, equals y hashCode de Lombok
@Builder // Permite usar el patrón de diseño Builder para crear instancias
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
public class RegisterRequest {

    private String email;    // El correo electrónico del nuevo usuario
    private String password; // La contraseña del nuevo usuario
    private Role role;       // El rol inicial que se asignará al usuario (ej., DOCTOR, RECEPTIONIST)
}