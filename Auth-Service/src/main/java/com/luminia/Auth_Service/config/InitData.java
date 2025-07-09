package com.luminia.Auth_Service.config;


import com.luminia.Auth_Service.model.Role;
import com.luminia.Auth_Service.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class InitData {
    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            log.info("Initializing roles in database...");
            
            if (!roleRepository.existsByName("ADMIN")) {
                Role adminRole = Role.builder().name("ADMIN").description("Administrador de consultorio").build();
                roleRepository.save(adminRole);
                log.info("Created ADMIN role with ID: {}", adminRole.getId());
            } else {
                log.info("ADMIN role already exists");
            }
            
            if (!roleRepository.existsByName("DOCTOR")) {
                Role doctorRole = Role.builder().name("DOCTOR").description("Médico").build();
                roleRepository.save(doctorRole);
                log.info("Created DOCTOR role with ID: {}", doctorRole.getId());
            } else {
                log.info("DOCTOR role already exists");
            }
            
            if (!roleRepository.existsByName("ASSISTANT")) {
                Role assistantRole = Role.builder().name("ASSISTANT").description("Asistente").build();
                roleRepository.save(assistantRole);
                log.info("Created ASSISTANT role with ID: {}", assistantRole.getId());
            } else {
                log.info("ASSISTANT role already exists");
            }
            
            log.info("Role initialization completed");
        };
    }
}