package com.luminia.user_service.client;


import com.luminia.user_service.dto.RoleAssignmentRequest;
import com.luminia.user_service.dto.UserRegistrationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

// El nombre 'auth-service' debe coincidir con el nombre de aplicación de AuthService en Eureka
@FeignClient(name = "Auth-Service")
public interface AuthServiceFeignClient {

    // Endpoint para registrar un nuevo usuario en AuthService (personal interno)
    @PostMapping("/api/auth/register-internal")
    // AuthServiceUserDto es un DTO de respuesta que AuthService podría enviar,
    // conteniendo el ID del usuario creado en AuthService.
    AuthServiceUserDto registerInternalUser(@RequestBody UserRegistrationRequest authUserDto);

    // Endpoint para asignar un rol a un usuario existente en AuthService
    @PutMapping("/api/auth/users/{userId}/assign-role")
    void assignRoleToAuthUser(@PathVariable("userId") Long userId, @RequestBody RoleAssignmentRequest roleRequest);

    // Endpoint para deshabilitar/eliminar un usuario en AuthService
    @DeleteMapping("/api/auth/users/{userId}/disable") // O /delete si es eliminación física
    void disableAuthUser(@PathVariable("userId") Long userId);

    // DTOs de respuesta de AuthService (tendrías que definir estas clases en UserService también)
    // Son simplificaciones, en un proyecto real tendrían más campos.
    class AuthServiceUserDto {
        public Long id;
        public String username;
        public String roleName;
        public boolean isActive;
    }

    class AuthServiceRoleDto {
        public Long id;
        public String name;
        // Otras propiedades como permisos si se necesitan consultar
    }
}