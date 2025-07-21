package com.jc.clinical_managment.auth_service.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    private String phoneNumber;

    private Date bornDate;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "hash_password")
    private String hashPassword;


    @Column(name = "enabled")
    private boolean enabled;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_consultorios", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "consultorio_id")
    private Set<Long> consultorioIds = new HashSet<>();
}