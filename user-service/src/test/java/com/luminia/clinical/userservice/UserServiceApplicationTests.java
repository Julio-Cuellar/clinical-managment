package com.luminia.clinical.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
	"spring.cloud.config.enabled=false",
	"spring.cloud.discovery.enabled=false",
	"spring.config.import="
})
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
		// Test that the Spring context loads successfully
	}
}