package com.luminia.Auth_Service.repository;

import com.luminia.Auth_Service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad User.
 * Proporciona métodos CRUD y de consulta personalizados para interactuar con la base de datos.
 * Extiende JpaRepository para obtener funcionalidades ORM básicas.
 */
@Repository // Marca esta interfaz como un componente de repositorio de Spring
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su dirección de correo electrónico.
     * @param email La dirección de correo electrónico del usuario.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no.
     */
    Optional<User> findByEmail(String email);

    /**
     * Verifica si existe un usuario con la dirección de correo electrónico especificada.
     * @param email La dirección de correo electrónico a verificar.
     * @return true si existe un usuario con ese email, false en caso contrario.
     */
    boolean existsByEmail(String email);
}