package com.luminia.Auth_Service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Inyecta el valor de la propiedad 'app.jwt.secret' definida en application.yml o Config Server
    @Value("${app.jwt.secret}")
    private String secretKeyString;

    // Inyecta el valor de la propiedad 'app.jwt.expiration-ms' (tiempo de vida del token normal)
    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    // Inyecta el valor de la propiedad 'app.jwt.refresh-expiration-ms' (tiempo de vida del token de refresco)
    @Value("${app.jwt.refresh-expiration-ms}")
    private long jwtRefreshExpirationMs; // Añadido para completar la inyección, aunque no se usa en generateToken aquí.

    // Se inicializará la clave real de firma una vez que secretKeyString esté disponible
    private SecretKey signingKey;

    /**
     * Obtiene la SecretKey para la firma y verificación de JWTs.
     * La inicializa solo una vez de forma lazy (cuando se solicita por primera vez).
     * @return La clave secreta.
     * @throws IllegalStateException Si la propiedad 'app.jwt.secret' no está configurada o está vacía.
     */
    private Key getSigningKey() {
        if (signingKey == null) {
            if (secretKeyString == null || secretKeyString.isEmpty()) {
                throw new IllegalStateException("La propiedad 'app.jwt.secret' no está configurada o está vacía. Por favor, define esta propiedad en tu archivo de configuración o como variable de entorno.");
            }
            byte[] keyBytes = Decoders.BASE64.decode(secretKeyString);
            signingKey = Keys.hmacShaKeyFor(keyBytes);
        }
        return signingKey;
    }

    /**
     * Extrae un claim específico del token JWT.
     * @param token El token JWT.
     * @param claimsResolver Función para resolver el claim deseado.
     * @param <T> Tipo del claim.
     * @return El valor del claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todos los claims del token JWT.
     * @param token El token JWT.
     * @return Un objeto Claims que contiene todos los claims.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey()) // Usa verifyWith() y castea a SecretKey
                .build()
                .parseSignedClaims(token) // Usa parseSignedClaims()
                .getPayload(); // Usa getPayload() para obtener los claims (antes getBody())
    }

    /**
     * Extrae el nombre de usuario (sujeto) del token JWT.
     * @param token El token JWT.
     * @return El nombre de usuario (email).
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae la fecha de expiración del token JWT.
     * @param token El token JWT.
     * @return La fecha de expiración.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Verifica si el token ha expirado.
     * @param token El token JWT.
     * @return true si el token ha expirado, false en caso contrario.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Genera un token JWT para un usuario utilizando el tiempo de expiración configurado.
     * @param username El nombre de usuario (email) para el token.
     * @return El token JWT generado.
     */
    public String generateToken(String username) {
        return generateToken(username, jwtExpirationMs);
    }

    /**
     * Genera un token JWT para un usuario con una duración específica.
     * Este método es útil si necesitas generar tokens con duraciones diferentes (ej. refresh token).
     * @param username El nombre de usuario (email) para el token.
     * @param expirationMillis El tiempo de expiración en milisegundos desde el momento actual.
     * @return El token JWT generado.
     */
    public String generateToken(String username, long expirationMillis) {
        return Jwts.builder()
                .setSubject(username) // Sujeto del token (username/email)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis)) // Fecha de expiración
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Firma el token con la clave y algoritmo HS256
                .compact(); // Construye el token como una cadena compacta
    }

    /**
     * Valida un token JWT contra los detalles de un usuario.
     * @param token El token JWT a validar.
     * @param userDetails Los detalles del usuario obtenidos de UserDetailsService.
     * @return true si el token es válido para el usuario, false en caso contrario.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String tokenUsername = extractUsername(token);
        // El nombre de usuario del token debe coincidir con el del UserDetails, y el token no debe haber expirado.
        return (tokenUsername.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Puedes añadir un método para generar el Refresh Token si lo necesitas en tu AuthService
    public String generateRefreshToken(String username) {
        return generateToken(username, jwtRefreshExpirationMs);
    }
}