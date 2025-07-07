package com.luminia.user_service.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "clinic_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auth_service_user_id", nullable = false, unique = true)
    private Long authServiceUserId; // ID del usuario correspondiente en AuthService

    @Column(name = "clinic_id", nullable = false)
    private Long clinicId; // ID del consultorio al que pertenece este usuario

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email; // Correo electrónico interno para este perfil

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "role_name", nullable = false)
    private String roleName; // Rol asignado dentro del consultorio (ej. DOCTOR, RECEPCIONIST)

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Considerar un campo 'isActive' para deshabilitar lógicamente
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}