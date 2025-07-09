package com.luminia.Auth_Service.service;

import com.luminia.Auth_Service.dto.RegisterRequest;
import com.luminia.Auth_Service.model.Role;
import com.luminia.Auth_Service.model.User;
import com.luminia.Auth_Service.repository.RoleRepository;
import com.luminia.Auth_Service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.cloud.config.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "eureka.client.enabled=false"
})
class UserRolePersistenceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void userWithRoles_shouldPersistCorrectly() {
        // Setup roles
        Role adminRole = Role.builder()
                .name("ADMIN")
                .description("Administrator")
                .build();
        entityManager.persistAndFlush(adminRole);

        // Create user with role
        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .passwordHash(new BCryptPasswordEncoder().encode("password"))
                .enabled(true)
                .build();

        // Add role to user
        user.getRoles().add(adminRole);
        
        // Save and flush
        User savedUser = userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        // Verify persistence
        User retrievedUser = userRepository.findById(savedUser.getId()).orElse(null);
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getRoles()).isNotNull();
        assertThat(retrievedUser.getRoles()).isNotEmpty();
        
        Set<String> roleNames = retrievedUser.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        assertThat(roleNames).contains("ADMIN");
        
        System.out.println("User ID: " + retrievedUser.getId());
        System.out.println("User roles: " + roleNames);
        System.out.println("Test passed - roles are persisting correctly!");
    }
}