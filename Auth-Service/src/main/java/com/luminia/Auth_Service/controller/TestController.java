package com.luminia.Auth_Service.controller;

import com.luminia.Auth_Service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final AuthService authService;

    @GetMapping("/test")
    public String hello() {
        return "Auth service is running!";
    }

    @GetMapping("/test/verify-roles/{username}")
    public String verifyUserRoles(@PathVariable String username) {
        authService.verifyUserRoles(username);
        return "Role verification completed for user: " + username + ". Check logs for details.";
    }
}
