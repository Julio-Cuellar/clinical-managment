package com.luminia.Auth_Service.enums;

/**
 * Enumeración que define los roles de usuario dentro de la aplicación.
 * Estos roles se utilizarán para la asignación de permisos y control de acceso.
 */
public enum Role {
    ADMIN,          // Administrador general o del consultorio
    DOCTOR,         // Médico
    RECEPTIONIST,   // Recepcionista
    ASSISTANT;      // Asistente (p.ej., enfermera, auxiliar)
    // Se pueden añadir más roles personalizados dinámicamente en el futuro a nivel de aplicación si es necesario.
}