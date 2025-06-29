package com.luminia.Auth_Service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Clase de configuración principal para Spring Security.
 * Define las reglas de seguridad, la gestión de sesiones y la integración del filtro JWT.
 */
@Configuration
@EnableWebSecurity // Habilita el soporte de seguridad web de Spring Security
@RequiredArgsConstructor // Genera un constructor con los campos final inyectados
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter; // Nuestro filtro JWT personalizado
    private final AuthenticationProvider authenticationProvider; // El proveedor de autenticación definido en ApplicationConfig

    /**
     * Configura la cadena de filtros de seguridad HTTP.
     * @param http El objeto HttpSecurity para configurar la seguridad.
     * @return La SecurityFilterChain configurada.
     * @throws Exception Si ocurre un error durante la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilita CSRF ya que JWT maneja la autenticación sin estado (stateless)
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos: permite el acceso a todos sin autenticación
                        // Esto es crucial para los endpoints de registro y login.
                        .requestMatchers("/api/auth/**").permitAll() // Ejemplo: /api/auth/register, /api/auth/login
                        // Cualquier otra solicitud requiere autenticación
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        // Configura la gestión de sesiones para que sea sin estado (STATELESS)
                        // Esto es fundamental para JWT, ya que cada solicitud se autentica con el token.
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider) // Establece el AuthenticationProvider personalizado
                // Agrega nuestro filtro JWT personalizado antes del filtro de autenticación de usuario/contraseña estándar de Spring Security
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); // Construye y retorna la cadena de filtros de seguridad
    }
}