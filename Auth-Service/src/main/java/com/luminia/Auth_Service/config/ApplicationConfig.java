package com.luminia.Auth_Service.config;

import com.luminia.Auth_Service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Clase de configuración para beans relacionados con la seguridad de la aplicación,
 * como PasswordEncoder, UserDetailsService y AuthenticationProvider.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    /**
     * Define el bean UserDetailsService que Spring Security utilizará para cargar
     * los detalles del usuario a partir del email.
     * @return Una implementación de UserDetailsService.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        // Implementación que busca el usuario por email en el repositorio
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + username));
    }

    /**
     * Define el AuthenticationProvider. Usa DaoAuthenticationProvider para
     * autenticación basada en usuario/contraseña, utilizando el UserDetailsService
     * y el PasswordEncoder definidos.
     * @return Una implementación de AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService()); // Establece cómo obtener los detalles del usuario
        authProvider.setPasswordEncoder(passwordEncoder());     // Establece el codificador de contraseñas
        return authProvider;
    }

    /**
     * Expone el bean AuthenticationManager, que es el punto de entrada para realizar la autenticación.
     * @param config La configuración de autenticación de Spring Security.
     * @return El AuthenticationManager configurado.
     * @throws Exception Si ocurre un error durante la configuración.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Define el PasswordEncoder que se utilizará para codificar y verificar contraseñas.
     * Se recomienda BCryptPasswordEncoder por su seguridad.
     * @return Una instancia de PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}