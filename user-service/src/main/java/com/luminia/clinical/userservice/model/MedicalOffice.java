package com.luminia.clinical.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "medical_offices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalOffice {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Office name is required")
    @Size(max = 200, message = "Office name must not exceed 200 characters")
    @Column(nullable = false, length = 200)
    private String name;

    @Size(max = 500, message = "Location must not exceed 500 characters")
    @Column(length = 500)
    private String location;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @Column(nullable = false)
    private boolean active = true;
}