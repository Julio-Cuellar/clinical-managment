package com.luminia.Auth_Service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada lanzada cuando se intenta registrar un usuario
 * con un correo electrónico que ya existe en el sistema.
 * Mapea automáticamente a un estado HTTP 409 Conflict.
 */
@ResponseStatus(HttpStatus.CONFLICT) // Mapea esta excepción a un código de estado HTTP 409 CONFLICT
public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Constructor que acepta un mensaje para la excepción.
     * @param message El mensaje que describe la causa de la excepción.
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * Constructor que acepta un mensaje y la causa original de la excepción.
     * @param message El mensaje que describe la causa de la excepción.
     * @param cause La causa original de la excepción (Throwable).
     */
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}