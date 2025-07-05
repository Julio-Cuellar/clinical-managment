package com.luminia.Auth_Service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private boolean enabled;
    private String clinicName;
    private Set<String> roles;
}