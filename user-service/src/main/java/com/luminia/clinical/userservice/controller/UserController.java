package com.luminia.clinical.userservice.controller;

import com.luminia.clinical.userservice.dto.CreateUserRequest;
import com.luminia.clinical.userservice.dto.UpdateUserRequest;
import com.luminia.clinical.userservice.dto.UserResponse;
import com.luminia.clinical.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("Creating user: {}", request.getUsername());
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, 
                                                   @Valid @RequestBody UpdateUserRequest request) {
        log.info("Updating user: {}", id);
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('ASSISTANT') or #id == authentication.principal.id")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('ASSISTANT')")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        UserResponse response = userService.getUserByUsername(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('ASSISTANT')")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        UserResponse response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<Page<UserResponse>> getAllUsers(@PageableDefault(size = 20) Pageable pageable) {
        Page<UserResponse> response = userService.getAllUsers(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<Page<UserResponse>> searchUsers(@RequestParam String q, 
                                                         @PageableDefault(size = 20) Pageable pageable) {
        Page<UserResponse> response = userService.searchUsers(q, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/clinic/{clinicId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<List<UserResponse>> getUsersByClinic(@PathVariable Long clinicId) {
        List<UserResponse> response = userService.getUsersByClinic(clinicId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/role/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getUsersByRole(@PathVariable String roleName) {
        List<UserResponse> response = userService.getUsersByRole(roleName);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/office/{officeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<List<UserResponse>> getUsersByOffice(@PathVariable Long officeId) {
        List<UserResponse> response = userService.getUsersByOffice(officeId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> enableUser(@PathVariable Long id) {
        log.info("Enabling user: {}", id);
        userService.enableUser(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> disableUser(@PathVariable Long id) {
        log.info("Disabling user: {}", id);
        userService.disableUser(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> lockUser(@PathVariable Long id) {
        log.info("Locking user: {}", id);
        userService.lockUser(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/unlock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> unlockUser(@PathVariable Long id) {
        log.info("Unlocking user: {}", id);
        userService.unlockUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignRolesToUser(@PathVariable Long id, 
                                                  @RequestBody List<Long> roleIds) {
        log.info("Assigning roles to user {}: {}", id, roleIds);
        userService.assignRolesToUser(id, roleIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeRolesFromUser(@PathVariable Long id, 
                                                    @RequestBody List<Long> roleIds) {
        log.info("Removing roles from user {}: {}", id, roleIds);
        userService.removeRolesFromUser(id, roleIds);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/offices")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<Void> assignOfficesToUser(@PathVariable Long id, 
                                                    @RequestBody List<Long> officeIds) {
        log.info("Assigning offices to user {}: {}", id, officeIds);
        userService.assignOfficesToUser(id, officeIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/offices")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<Void> removeOfficesFromUser(@PathVariable Long id, 
                                                      @RequestBody List<Long> officeIds) {
        log.info("Removing offices from user {}: {}", id, officeIds);
        userService.removeOfficesFromUser(id, officeIds);
        return ResponseEntity.ok().build();
    }
}