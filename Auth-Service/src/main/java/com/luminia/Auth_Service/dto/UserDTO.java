package com.luminia.Auth_Service.dto;

import com.luminia.Auth_Service.enums.Role; // Importa tu enum Role
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar la información de un usuario,
 * utilizado para exponer datos de usuario a través de la API
 * sin incluir campos sensibles como la contraseña.
 */
@Data // Genera getters, setters, toString, equals y hashCode de Lombok
@Builder // Permite usar el patrón de diseño Builder para crear instancias
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
public class UserDTO {

    private Long id;         // ID del usuario
    private String email;    // Correo electrónico del usuario
    private Role role;       // Rol del usuario

    // Puedes añadir otros campos que consideres relevantes exponer, como nombre, apellido, etc.
    // private String firstName;
    // private String lastName;
}