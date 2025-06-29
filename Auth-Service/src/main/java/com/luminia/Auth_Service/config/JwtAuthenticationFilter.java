package com.luminia.Auth_Service.config;

import com.luminia.Auth_Service.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro personalizado para la autenticación basada en JWT.
 * Intercepta las solicitudes, extrae y valida el JWT, y establece el contexto de seguridad de Spring.
 */
@Component
@RequiredArgsConstructor // Genera un constructor con los campos final inyectados
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService; // Su implementación de UserDetailsService

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization"); // Obtiene el encabezado Authorization
        final String jwt;
        final String userEmail;

        // 1. Verifica si el encabezado Authorization es válido y contiene un token Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Si no, pasa al siguiente filtro
            return;
        }

        jwt = authHeader.substring(7); // Extrae el token JWT (después de "Bearer ")
        userEmail = jwtUtil.extractUsername(jwt); // Extrae el email del usuario del token

        // 2. Si se extrajo un email de usuario Y no hay autenticación actual en el contexto de seguridad
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carga los detalles del usuario usando el UserDetailsService
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 3. Valida el token JWT contra los detalles del usuario
            if (jwtUtil.validateToken(jwt, userDetails)) {
                // Si el token es válido, crea un objeto de autenticación
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Las credenciales son null para JWT, ya validadas por el token
                        userDetails.getAuthorities() // Obtiene los roles/autoridades del usuario
                );
                // Establece detalles adicionales de la solicitud en el token de autenticación
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // Establece el objeto de autenticación en el SecurityContextHolder
                // Esto indica a Spring Security que el usuario está autenticado para esta solicitud
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response); // Continúa con la cadena de filtros
    }
}