package com.jc.clinical_managment.common.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequest {
    private String username;
    private String userId;
    private String role;
    private String service;
    private Map<String, Object> additionalClaims;
}