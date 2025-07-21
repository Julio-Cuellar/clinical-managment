package com.jc.clinical_managment.auth_service.dto;

import com.jc.clinical_managment.auth_service.domain.Role;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String name;
    private String lastName;
    private String email;
    private String username;
    private String phoneNumber;
    private Date bornDate;
    private Boolean enabled;
    private String password;
    private Role role;
}