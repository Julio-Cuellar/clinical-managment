package com.jc.clinical_managment.consultorio_service.client;

import com.jc.clinical_managment.common.security.jwt.RS256JwtService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FeignClientConfig {
    
    private final RS256JwtService jwtService;
    
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                
                if (authentication != null && authentication.isAuthenticated()) {
                    // For service-to-service communication, we can:
                    // 1. Forward the existing token
                    // 2. Generate a new service token
                    
                    // For now, let's generate a service token
                    String serviceToken = jwtService.generateServiceToken(
                        "consultorio-service", 
                        new String[]{"agenda:create", "agenda:read"}
                    );
                    
                    requestTemplate.header("Authorization", "Bearer " + serviceToken);
                    log.debug("Added service token to Feign request");
                }
            }
        };
    }
}