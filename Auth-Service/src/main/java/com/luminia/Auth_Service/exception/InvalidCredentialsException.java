package com.luminia.Auth_Service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada lanzada cuando las credenciales proporcionadas
 * para la autenticación (email/contraseña) son inválidas.
 * Mapea automáticamente a un estado HTTP 401 Unauthorized.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED) // Mapea esta excepción a un código de estado HTTP 401 UNAUTHORIZED
public class InvalidCredentialsException extends RuntimeException {

    /**
     * Constructor que acepta un mensaje para la excepción.
     * @param message El mensaje que describe la causa de la excepción.
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }

    /**
     * Constructor que acepta un mensaje y la causa original de la excepción.
     * @param message El mensaje que describe la causa de la excepción.
     * @param cause La causa original de la excepción (Throwable).
     */
    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}