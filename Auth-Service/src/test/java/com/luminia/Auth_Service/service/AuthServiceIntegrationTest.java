package com.luminia.Auth_Service.service;

import com.luminia.Auth_Service.config.InitData;
import com.luminia.Auth_Service.dto.RegisterRequest;
import com.luminia.Auth_Service.model.Role;
import com.luminia.Auth_Service.model.User;
import com.luminia.Auth_Service.repository.ClinicRepository;
import com.luminia.Auth_Service.repository.RoleRepository;
import com.luminia.Auth_Service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(InitData.class)
@ComponentScan(basePackages = {"com.luminia.Auth_Service.service", "com.luminia.Auth_Service.config"})
class AuthServiceIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    private AuthService authService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(userRepository, roleRepository, clinicRepository, passwordEncoder, null);
        
        // Initialize roles manually if not already present
        if (!roleRepository.existsByName("ADMIN")) {
            roleRepository.save(Role.builder().name("ADMIN").description("Administrador de consultorio").build());
        }
        if (!roleRepository.existsByName("DOCTOR")) {
            roleRepository.save(Role.builder().name("DOCTOR").description("Médico").build());
        }
        if (!roleRepository.existsByName("ASSISTANT")) {
            roleRepository.save(Role.builder().name("ASSISTANT").description("Asistente").build());
        }
        entityManager.flush();
    }

    @Test
    void registerNewUser_shouldCreateUserWithAdminRole() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("admin-test");
        request.setEmail("admin@test.com");
        request.setPassword("password123");
        request.setClinicName("Test Clinic");

        // When
        User registeredUser = authService.registerNewUser(request);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(registeredUser).isNotNull();
        assertThat(registeredUser.getId()).isNotNull();
        assertThat(registeredUser.getUsername()).isEqualTo("admin-test");
        assertThat(registeredUser.getEmail()).isEqualTo("admin@test.com");
        assertThat(registeredUser.isEnabled()).isTrue();

        // Verify user is persisted in database
        User userFromDb = userRepository.findByUsername("admin-test").orElse(null);
        assertThat(userFromDb).isNotNull();
        assertThat(userFromDb.getId()).isEqualTo(registeredUser.getId());

        // Verify ADMIN role is assigned and persisted
        assertThat(userFromDb.getRoles()).isNotNull();
        assertThat(userFromDb.getRoles()).isNotEmpty();
        
        Set<String> roleNames = userFromDb.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        assertThat(roleNames).contains("ADMIN");
        
        // Verify role was properly saved in user_roles table
        Role adminRole = roleRepository.findByName("ADMIN").orElse(null);
        assertThat(adminRole).isNotNull();
        assertThat(userFromDb.getRoles()).contains(adminRole);
        
        System.out.println("User ID: " + userFromDb.getId());
        System.out.println("User roles: " + roleNames);
        System.out.println("Admin role ID: " + adminRole.getId());
    }

    @Test
    void registerNewUser_shouldVerifyRolesExistInDatabase() {
        // Verify that roles are properly initialized
        Role adminRole = roleRepository.findByName("ADMIN").orElse(null);
        Role doctorRole = roleRepository.findByName("DOCTOR").orElse(null);
        Role assistantRole = roleRepository.findByName("ASSISTANT").orElse(null);

        assertThat(adminRole).isNotNull();
        assertThat(doctorRole).isNotNull();
        assertThat(assistantRole).isNotNull();

        System.out.println("ADMIN role: " + adminRole);
        System.out.println("DOCTOR role: " + doctorRole);
        System.out.println("ASSISTANT role: " + assistantRole);
    }
}