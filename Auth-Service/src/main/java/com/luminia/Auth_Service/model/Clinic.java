package com.luminia.Auth_Service.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "clinics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clinic {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;
    private String contactInfo;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}