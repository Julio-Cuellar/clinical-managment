package com.luminia.Auth_Service.entity;

import com.luminia.Auth_Service.enums.Role;
import jakarta.persistence.*; // Importaciones de Jakarta Persistence para Spring Boot 3+
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Entidad que representa a un usuario en la base de datos.
 * Esta clase mapea a la tabla 'users' en MySQL.
 * También implementa UserDetails de Spring Security para manejar la autenticación y autorización.
 */
@Data // Genera getters, setters, toString, equals y hashCode de Lombok
@Builder // Permite usar el patrón de diseño Builder para crear instancias
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
@Entity // Declara esta clase como una entidad JPA
@Table(name = "users") // Especifica el nombre de la tabla en la base de datos
public class User implements UserDetails {

    @Id // Marca 'id' como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la generación automática de ID (auto-incremento en MySQL)
    private Long id;

    @Column(unique = true, nullable = false) // 'email' debe ser único y no nulo
    private String email;

    @Column(nullable = false) // 'password' no puede ser nulo
    private String password;

    @Enumerated(EnumType.STRING) // Almacena el enum 'Role' como String en la BD (ej. "ADMIN", "DOCTOR")
    @Column(nullable = false) // 'role' no puede ser nulo
    private Role role;

    // --- Implementación de UserDetails de Spring Security ---

    /**
     * Retorna las autoridades (roles) concedidas al usuario.
     * @return Una colección de GrantedAuthority.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convierte el enum Role en una autoridad de Spring Security
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Retorna el nombre de usuario (en este caso, el email).
     * @return El email del usuario.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Retorna la contraseña utilizada para autenticar al usuario.
     * @return La contraseña del usuario.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Indica si la cuenta del usuario no ha expirado.
     * @return true si la cuenta es válida (no ha expirado).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; // Para este ejemplo, siempre es true. En una app real, podría haber lógica de expiración de cuenta.
    }

    /**
     * Indica si la cuenta del usuario no está bloqueada.
     * @return true si la cuenta no está bloqueada.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true; // Para este ejemplo, siempre es true. En una app real, podría haber lógica de bloqueo de cuenta.
    }

    /**
     * Indica si las credenciales del usuario (contraseña) no han expirado.
     * @return true si las credenciales son válidas.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Para este ejemplo, siempre es true. En una app real, podría haber lógica de expiración de credenciales.
    }

    /**
     * Indica si el usuario está habilitado (activo).
     * @return true si el usuario está habilitado.
     */
    @Override
    public boolean isEnabled() {
        return true; // Para este ejemplo, siempre es true. En una app real, podría haber lógica de habilitación/deshabilitación.
    }
}