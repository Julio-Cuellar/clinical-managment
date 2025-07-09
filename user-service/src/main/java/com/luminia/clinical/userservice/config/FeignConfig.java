package com.luminia.clinical.userservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@Slf4j
public class FeignConfig {
    
    @Value("${auth.service.client-id:user-service}")
    private String clientId;
    
    @Value("${auth.service.client-secret:user-service-secret}")
    private String clientSecret;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // Forward the Authorization header from the current request
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.getCredentials() != null) {
                    String token = authentication.getCredentials().toString();
                    if (token != null && !token.isEmpty()) {
                        if (!token.startsWith("Bearer ")) {
                            token = "Bearer " + token;
                        }
                        template.header("Authorization", token);
                        log.debug("Added Authorization header to Feign request");
                    }
                } else {
                    log.warn("No authentication found in SecurityContext for Feign request");
                }
            }
        };
    }
}