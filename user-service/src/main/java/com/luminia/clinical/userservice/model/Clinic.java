package com.luminia.clinical.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "clinics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clinic {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Clinic name is required")
    @Size(max = 200, message = "Clinic name must not exceed 200 characters")
    @Column(nullable = false, length = 200)
    private String name;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    @Column(length = 500)
    private String address;

    @Size(max = 255, message = "Contact info must not exceed 255 characters")
    @Column(name = "contact_info", length = 255)
    private String contactInfo;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}