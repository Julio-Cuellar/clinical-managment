package com.luminia.user_service.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicUserDto {

    private Long id; // Para respuestas, no para creación

    private Long authServiceUserId; // Se llenará después de la comunicación con AuthService

    @NotBlank(message = "El nombre no puede estar vacío")
    private String firstName;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String lastName;

    @NotBlank(message = "El correo electrónico interno no puede estar vacío")
    @Email(message = "Formato de correo electrónico inválido")
    private String email; // Correo interno para el personal

    private String phoneNumber;

    @NotBlank(message = "El rol no puede estar vacío")
    private String roleName; // Ej. "DOCTOR", "RECEPCIONIST", "ASSISTANT"

    // clinicId no se incluye aquí para la creación/actualización directa, se pasa por la URL o el servicio
}