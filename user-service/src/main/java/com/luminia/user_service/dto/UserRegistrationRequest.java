package com.luminia.user_service.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    @NotBlank
    @Email
    private String username; // Será el email interno del ClinicUser
    @NotBlank
    private String password;
    @NotBlank
    private String roleName; // Rol inicial para el AuthService (ej. "CLINIC_STAFF", "CLINIC_ADMIN")
}