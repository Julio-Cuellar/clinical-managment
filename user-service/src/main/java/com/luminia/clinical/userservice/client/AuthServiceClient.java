package com.luminia.clinical.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", url = "${auth.service.url:http://localhost:8081}")
public interface AuthServiceClient {

    @GetMapping("/api/validate/{username}")
    ResponseEntity<Boolean> validateUser(@PathVariable String username, 
                                       @RequestHeader("Authorization") String token);

    @GetMapping("/api/user/{username}/roles")
    ResponseEntity<String[]> getUserRoles(@PathVariable String username, 
                                        @RequestHeader("Authorization") String token);
}