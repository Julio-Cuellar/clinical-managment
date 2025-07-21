package com.jc.clinical_managment.auth_service.dto;

import com.jc.clinical_managment.auth_service.domain.Role;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String username;
    private String phoneNumber;
    private Date bornDate;
    private boolean enabled;
    private Role role;
    private Set<Long> consultorioIds;
}