package com.luminia.user_service.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Genera getters, setters, toString, equals, hashCode
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
public class ClinicDto {

    private Long id; // Para respuestas, no para creación

    @NotBlank(message = "El nombre del consultorio no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;

    @NotBlank(message = "La dirección no puede estar vacía")
    private String address;

    @NotBlank(message = "La información de contacto no puede estar vacía")
    private String contactInfo;

    // ownerUserId no se incluye aquí, se obtiene del contexto de seguridad o se pasa por el servicio
}