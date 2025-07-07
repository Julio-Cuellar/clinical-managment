package com.luminia.user_service.controller;


import com.luminia.user_service.dto.ClinicDto;
import com.luminia.user_service.model.Clinic;
import com.luminia.user_service.service.ClinicService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/clinics")
public class ClinicController {

    private final ClinicService clinicService;

    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @PostMapping
    public ResponseEntity<Clinic> createClinic(@Valid @RequestBody ClinicDto clinicDto) {
        // En un entorno real, el ownerAuthServiceUserId se obtendría del token JWT
        // del usuario autenticado que está haciendo la solicitud.
        // Por ahora, lo simulamos con un valor fijo o lo pasamos como parámetro.
        Long ownerAuthServiceUserId = 1L; // SIMULADO: Obtener del contexto de seguridad real
        Clinic createdClinic = clinicService.createClinic(clinicDto, ownerAuthServiceUserId);
        return new ResponseEntity<>(createdClinic, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clinic> getClinicById(@PathVariable Long id) {
        return clinicService.getClinicById(id)
                .map(clinic -> new ResponseEntity<>(clinic, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clinic> updateClinic(@PathVariable Long id, @Valid @RequestBody ClinicDto clinicDto) {
        try {
            Clinic updatedClinic = clinicService.updateClinic(id, clinicDto);
            return new ResponseEntity<>(updatedClinic, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClinic(@PathVariable Long id) {
        try {
            clinicService.deleteClinic(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/owner/{ownerAuthServiceUserId}")
    public ResponseEntity<List<Clinic>> getClinicsByOwner(@PathVariable Long ownerAuthServiceUserId) {
        List<Clinic> clinics = clinicService.getClinicsByOwner(ownerAuthServiceUserId);
        return new ResponseEntity<>(clinics, HttpStatus.OK);
    }
}