package com.luminia.user_service.service;


import com.luminia.user_service.dto.ClinicDto;
import com.luminia.user_service.model.Clinic;
import com.luminia.user_service.repository.ClinicRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ClinicServiceImpl implements ClinicService {

    private final ClinicRepository clinicRepository;
    // Inyectar otros servicios si se requiere inicialización de componentes esenciales
    // private final AppointmentServiceFeignClient appointmentService;
    // private final PatientServiceFeignClient patientService;
    // ...

    public ClinicServiceImpl(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

    @Override
    @Transactional
    public Clinic createClinic(ClinicDto clinicDto, Long ownerAuthServiceUserId) {
        // Validar si ya existe un consultorio con el mismo nombre (opcional)
        if (clinicRepository.findByName(clinicDto.getName()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un consultorio con este nombre.");
        }

        Clinic clinic = new Clinic();
        BeanUtils.copyProperties(clinicDto, clinic);
        clinic.setOwnerUserId(ownerAuthServiceUserId);
        clinic.setActive(true); // Por defecto activo

        Clinic savedClinic = clinicRepository.save(clinic);

        // --- Lógica para inicializar componentes esenciales en otros microservicios ---
        // Esto implicaría llamadas a Feign Clients de otros servicios.
        // Por ejemplo:
        // appointmentService.initializeClinicAgenda(savedClinic.getId());
        // patientService.initializeClinicCatalog(savedClinic.getId());
        // ...
        // Asegúrate de manejar posibles fallos en estas inicializaciones.

        return savedClinic;
    }

    @Override
    public Optional<Clinic> getClinicById(Long clinicId) {
        return clinicRepository.findById(clinicId);
    }

    @Override
    @Transactional
    public Clinic updateClinic(Long clinicId, ClinicDto clinicDto) {
        Clinic existingClinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new NoSuchElementException("Consultorio no encontrado con ID: " + clinicId));

        BeanUtils.copyProperties(clinicDto, existingClinic, "id", "ownerUserId", "createdAt"); // No sobrescribir ID, owner, ni fecha de creación
        return clinicRepository.save(existingClinic);
    }

    @Override
    @Transactional
    public void deleteClinic(Long clinicId) {
        // En un sistema real, se preferiría una eliminación lógica (isActive = false)
        // en lugar de una eliminación física para mantener la integridad referencial
        // y el historial.

        // Si se decide eliminar físicamente, es CRUCIAL eliminar datos relacionados
        // en CASCADA en otros microservicios para evitar datos huérfanos.
        // Esto requeriría llamadas a Feign Clients de:
        // - ClinicUserService (eliminar ClinicUsers asociados)
        // - PatientService (eliminar pacientes y expedientes asociados)
        // - AppointmentService (eliminar citas y agendas asociadas)
        // - InventoryService (eliminar inventario asociado)
        // - FinanceService (eliminar transacciones financieras asociadas)
        // - AuthService (deshabilitar/eliminar usuarios de AuthService vinculados a ClinicUsers eliminados)

        // Ejemplo de eliminación lógica:
        Clinic clinicToDelete = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new NoSuchElementException("Consultorio no encontrado con ID: " + clinicId));
        clinicToDelete.setActive(false);
        clinicRepository.save(clinicToDelete);

        // Notificar a otros servicios que la clínica ha sido deshabilitada/eliminada
        // Esto podría hacerse con eventos asíncronos (Kafka/RabbitMQ) para mayor robustez.
        // O llamadas síncronas a Feign Clients:
        // clinicUserService.disableClinicUsersByClinicId(clinicId);
        // patientService.disablePatientsByClinicId(clinicId);
        // ...
    }

    @Override
    public List<Clinic> getClinicsByOwner(Long ownerAuthServiceUserId) {
        return clinicRepository.findByOwnerUserId(ownerAuthServiceUserId);
    }
}