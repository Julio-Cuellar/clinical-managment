package com.luminia.clinical.userservice.config;

import com.luminia.clinical.userservice.model.Role;
import com.luminia.clinical.userservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class InitData {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (!roleRepository.existsByName("ADMIN")) {
                roleRepository.save(Role.builder()
                        .name("ADMIN")
                        .description("Administrator with full system access")
                        .build());
            }
            if (!roleRepository.existsByName("DOCTOR")) {
                roleRepository.save(Role.builder()
                        .name("DOCTOR")
                        .description("Medical doctor with clinical access")
                        .build());
            }
            if (!roleRepository.existsByName("NURSE")) {
                roleRepository.save(Role.builder()
                        .name("NURSE")
                        .description("Nurse with patient care access")
                        .build());
            }
            if (!roleRepository.existsByName("RECEPTIONIST")) {
                roleRepository.save(Role.builder()
                        .name("RECEPTIONIST")
                        .description("Receptionist with front desk access")
                        .build());
            }
            if (!roleRepository.existsByName("ASSISTANT")) {
                roleRepository.save(Role.builder()
                        .name("ASSISTANT")
                        .description("Medical assistant with limited access")
                        .build());
            }
        };
    }
}