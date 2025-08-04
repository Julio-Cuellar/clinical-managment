package com.jc.clinical_managment.common.security.filter;

import com.jc.clinical_managment.common.security.jwt.RS256JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final RS256JwtService jwtService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                if (jwtService.isTokenValid(jwt)) {
                    username = jwtService.extractUsername(jwt);
                    
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        Claims claims = jwtService.extractClaims(jwt);
                        
                        // Create authorities based on role
                        String role = jwtService.extractRole(jwt);
                        List<SimpleGrantedAuthority> authorities = List.of(
                            new SimpleGrantedAuthority("ROLE_" + role)
                        );
                        
                        // Create authentication token with user context
                        UsernamePasswordAuthenticationToken authToken = 
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                        
                        // Add user context to details
                        UserContext userContext = UserContext.builder()
                            .username(username)
                            .userId(jwtService.extractUserId(jwt))
                            .role(role)
                            .service(jwtService.extractService(jwt))
                            .isServiceToken(jwtService.isServiceToken(jwt))
                            .build();
                        
                        authToken.setDetails(userContext);
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        
                        log.debug("Set authentication for user: {} with role: {}", username, role);
                    }
                }
            } catch (Exception e) {
                log.error("Cannot set user authentication: {}", e.getMessage());
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // Skip JWT filter for auth endpoints
        return path.startsWith("/auth/") || 
               path.startsWith("/actuator/") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/swagger-ui");
    }
}