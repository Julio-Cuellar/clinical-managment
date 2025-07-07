package com.luminia.user_service.service;


import com.luminia.user_service.client.AuthServiceFeignClient;
import com.luminia.user_service.dto.ClinicUserDto;
import com.luminia.user_service.dto.RoleAssignmentRequest;
import com.luminia.user_service.dto.UserRegistrationRequest;
import com.luminia.user_service.model.ClinicUser;
import com.luminia.user_service.repository.ClinicRepository;
import com.luminia.user_service.repository.ClinicUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ClinicUserServiceImpl implements ClinicUserService {

    private final ClinicUserRepository clinicUserRepository;
    private final ClinicRepository clinicRepository; // Para validar que la clínica existe
    private final AuthServiceFeignClient authServiceFeignClient;

    public ClinicUserServiceImpl(ClinicUserRepository clinicUserRepository,
                                 ClinicRepository clinicRepository,
                                 AuthServiceFeignClient authServiceFeignClient) {
        this.clinicUserRepository = clinicUserRepository;
        this.clinicRepository = clinicRepository;
        this.authServiceFeignClient = authServiceFeignClient;
    }

    @Override
    @Transactional
    public ClinicUser createInternalUser(Long clinicId, ClinicUserDto userDto) {
        // 1. Validar que la clínica existe
        clinicRepository.findById(clinicId)
                .orElseThrow(() -> new NoSuchElementException("Consultorio no encontrado con ID: " + clinicId));

        // 2. Validar que el email interno no esté ya en uso para esta clínica (o globalmente si se prefiere)
        if (clinicUserRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El correo electrónico interno ya está en uso.");
        }

        // 3. Crear el usuario en AuthService (para autenticación)
        // Generar una contraseña inicial segura aquí o pedirla al cliente
        String initialPassword = generateRandomPassword(); // Implementar esta función
        UserRegistrationRequest authUserRequest = new UserRegistrationRequest(
                userDto.getEmail(),
                initialPassword,
                userDto.getRoleName() // El rol que se asignará en AuthService
        );

        AuthServiceFeignClient.AuthServiceUserDto authUserResponse = authServiceFeignClient.registerInternalUser(authUserRequest);

        // 4. Crear el ClinicUser en UserService
        ClinicUser clinicUser = new ClinicUser();
        BeanUtils.copyProperties(userDto, clinicUser, "id", "authServiceUserId"); // No copiar ID ni authServiceUserId del DTO
        clinicUser.setClinicId(clinicId);
        clinicUser.setAuthServiceUserId(authUserResponse.id); // Asignar el ID retornado por AuthService
        clinicUser.setActive(true); // Por defecto activo

        return clinicUserRepository.save(clinicUser);
    }

    @Override
    public Optional<ClinicUser> getClinicUserById(Long clinicUserId) {
        return clinicUserRepository.findById(clinicUserId);
    }

    @Override
    @Transactional
    public ClinicUser updateClinicUser(Long clinicUserId, ClinicUserDto userDto) {
        ClinicUser existingClinicUser = clinicUserRepository.findById(clinicUserId)
                .orElseThrow(() -> new NoSuchElementException("Usuario de consultorio no encontrado con ID: " + clinicUserId));

        // Verificar si el rol ha cambiado para actualizar en AuthService
        if (!existingClinicUser.getRoleName().equals(userDto.getRoleName())) {
            authServiceFeignClient.assignRoleToAuthUser(
                    existingClinicUser.getAuthServiceUserId(),
                    new RoleAssignmentRequest(userDto.getRoleName())
            );
            existingClinicUser.setRoleName(userDto.getRoleName());
        }

        // Actualizar otros campos
        BeanUtils.copyProperties(userDto, existingClinicUser, "id", "clinicId", "authServiceUserId", "email", "createdAt");
        // No permitir cambiar email ni clinicId directamente desde aquí para evitar inconsistencias

        return clinicUserRepository.save(existingClinicUser);
    }

    @Override
    @Transactional
    public void deleteClinicUser(Long clinicUserId) {
        ClinicUser clinicUserToDelete = clinicUserRepository.findById(clinicUserId)
                .orElseThrow(() -> new NoSuchElementException("Usuario de consultorio no encontrado con ID: " + clinicUserId));

        // Deshabilitar el usuario en AuthService
        authServiceFeignClient.disableAuthUser(clinicUserToDelete.getAuthServiceUserId());

        // Eliminar lógicamente el ClinicUser
        clinicUserToDelete.setActive(false);
        clinicUserRepository.save(clinicUserToDelete);
        // O eliminar físicamente: clinicUserRepository.delete(clinicUserToDelete);
    }

    @Override
    public List<ClinicUser> getUsersByClinic(Long clinicId) {
        return clinicUserRepository.findByClinicId(clinicId);
    }

    @Override
    @Transactional
    public ClinicUser assignRoleToClinicUser(Long clinicUserId, String newRoleName) {
        ClinicUser clinicUser = clinicUserRepository.findById(clinicUserId)
                .orElseThrow(() -> new NoSuchElementException("Usuario de consultorio no encontrado con ID: " + clinicUserId));

        authServiceFeignClient.assignRoleToAuthUser(
                clinicUser.getAuthServiceUserId(),
                new RoleAssignmentRequest(newRoleName)
        );
        clinicUser.setRoleName(newRoleName); // Actualizar el rol en la entidad ClinicUser
        return clinicUserRepository.save(clinicUser);
    }

    // Helper method para generar contraseñas aleatorias
    private String generateRandomPassword() {
        // Implementar lógica para generar una contraseña segura y aleatoria
        // Por ejemplo, usar SecureRandom y un conjunto de caracteres.
        // En un entorno de producción, esta contraseña debería ser temporal y forzar un cambio.
        return "password123"; // ¡Solo para fines de ejemplo!
    }
}