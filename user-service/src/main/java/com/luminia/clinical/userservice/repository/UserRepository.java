package com.luminia.clinical.userservice.repository;

import com.luminia.clinical.userservice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    List<User> findByClinicId(Long clinicId);
    
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);
    
    @Query("SELECT u FROM User u WHERE u.enabled = :enabled")
    List<User> findByEnabled(@Param("enabled") boolean enabled);
    
    @Query("SELECT u FROM User u WHERE u.clinic.id = :clinicId AND u.enabled = true")
    List<User> findActiveUsersByClinic(@Param("clinicId") Long clinicId);
    
    @Query("SELECT u FROM User u JOIN u.accessibleOffices o WHERE o.id = :officeId")
    List<User> findByAccessibleOfficeId(@Param("officeId") Long officeId);
    
    @Query("SELECT u FROM User u WHERE " +
           "(:search IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<User> findBySearchTerm(@Param("search") String search, Pageable pageable);
}