package com.luminia.user_service.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "clinics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(name = "owner_user_id", nullable = false)
    private Long ownerUserId; // ID del usuario en AuthService que es el propietario

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Considerar un campo 'isActive' para deshabilitar lógicamente en lugar de eliminar
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}