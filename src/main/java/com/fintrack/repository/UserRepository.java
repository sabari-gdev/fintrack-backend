package com.fintrack.repository;

import com.fintrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User Repository - Data Access Layer
 * <p>
 * JpaRepository<User, Long>:
 * - User = Entity type
 * - Long = Primary key type
 * <p>
 * Spring automatically provides:
 * - save(user) - Insert/Update
 * - findById(id) - Find by ID
 * - findAll() - Get all users
 * - deleteById(id) - Delete by ID
 * - count() - Count records
 * - exists(id) - Check if exists
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email
     * Spring automatically implements this based on method name!
     * Naming convention: findBy + FieldName
     * <p>
     * Returns Optional<User>:
     * - If found: Optional contains the user
     * - If not found: Optional is empty (no null!)
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if user exists by email
     * Spring automatically implements this too!
     * Returns true if email exists, false otherwise
     */
    boolean existsByEmail(String email);
}