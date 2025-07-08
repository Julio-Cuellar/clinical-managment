package com.luminia.clinical.userservice.service;

import com.luminia.clinical.userservice.dto.CreateUserRequest;
import com.luminia.clinical.userservice.dto.UserResponse;
import com.luminia.clinical.userservice.model.Role;
import com.luminia.clinical.userservice.repository.RoleRepository;
import com.luminia.clinical.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.cloud.config.enabled=false",
    "spring.cloud.discovery.enabled=false",
    "spring.config.import="
})
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void createUser_shouldCreateUserWithRoles() {
        // Given
        Role doctorRole = roleRepository.findByName("DOCTOR").orElseThrow();
        
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setPhone("1234567890");
        request.setRoleIds(Set.of(doctorRole.getId()));
        request.setEnabled(true);

        // When
        UserResponse response = userService.createUser(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo("testuser");
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getFirstName()).isEqualTo("Test");
        assertThat(response.getLastName()).isEqualTo("User");
        assertThat(response.getPhone()).isEqualTo("1234567890");
        assertThat(response.isEnabled()).isTrue();
        assertThat(response.getRoles()).hasSize(1);
        assertThat(response.getRoles().iterator().next().getName()).isEqualTo("DOCTOR");
    }

    @Test
    void getUserByUsername_shouldReturnUser() {
        // Given
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testuser2");
        request.setEmail("test2@example.com");
        request.setFirstName("Test2");
        request.setLastName("User2");
        request.setEnabled(true);

        UserResponse createdUser = userService.createUser(request);

        // When
        UserResponse foundUser = userService.getUserByUsername("testuser2");

        // Then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(createdUser.getId());
        assertThat(foundUser.getUsername()).isEqualTo("testuser2");
        assertThat(foundUser.getEmail()).isEqualTo("test2@example.com");
    }
}