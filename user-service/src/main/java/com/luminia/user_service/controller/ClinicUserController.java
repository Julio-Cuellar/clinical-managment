package com.luminia.user_service.controller;


import com.luminia.user_service.dto.ClinicUserDto;
import com.luminia.user_service.model.ClinicUser;
import com.luminia.user_service.service.ClinicUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/clinics/{clinicId}/users")
public class ClinicUserController {

    private final ClinicUserService clinicUserService;

    public ClinicUserController(ClinicUserService clinicUserService) {
        this.clinicUserService = clinicUserService;
    }

    @PostMapping
    public ResponseEntity<ClinicUser> createInternalUser(@PathVariable Long clinicId,
                                                         @Valid @RequestBody ClinicUserDto userDto) {
        try {
            ClinicUser createdUser = clinicUserService.createInternalUser(clinicId, userDto);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Por ejemplo, si el email ya existe
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Si la clínica no existe
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ClinicUser> getClinicUserById(@PathVariable Long userId) {
        return clinicUserService.getClinicUserById(userId)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ClinicUser> updateClinicUser(@PathVariable Long userId,
                                                       @Valid @RequestBody ClinicUserDto userDto) {
        try {
            ClinicUser updatedUser = clinicUserService.updateClinicUser(userId, userDto);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si el rol es inválido, etc.
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteClinicUser(@PathVariable Long userId) {
        try {
            clinicUserService.deleteClinicUser(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<ClinicUser>> getUsersByClinic(@PathVariable Long clinicId) {
        List<ClinicUser> users = clinicUserService.getUsersByClinic(clinicId);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/{userId}/assign-role")
    public ResponseEntity<ClinicUser> assignRoleToClinicUser(@PathVariable Long userId,
                                                             @RequestParam String newRoleName) {
        try {
            ClinicUser updatedUser = clinicUserService.assignRoleToClinicUser(userId, newRoleName);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si el rol es inválido
        }
    }
}