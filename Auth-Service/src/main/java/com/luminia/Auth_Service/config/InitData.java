package com.luminia.Auth_Service.config;


import com.luminia.Auth_Service.model.Role;
import com.luminia.Auth_Service.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitData {
    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (!roleRepository.existsByName("ADMIN"))
                roleRepository.save(Role.builder().name("ADMIN").description("Administrador de consultorio").build());
            if (!roleRepository.existsByName("DOCTOR"))
                roleRepository.save(Role.builder().name("DOCTOR").description("Médico").build());
            if (!roleRepository.existsByName("ASSISTANT"))
                roleRepository.save(Role.builder().name("ASSISTANT").description("Asistente").build());
        };
    }
}