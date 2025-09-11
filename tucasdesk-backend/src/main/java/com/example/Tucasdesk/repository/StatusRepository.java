package com.example.Tucasdesk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Tucasdesk.model.Status;

/**
 * Repository interface for {@link Status} entities.
 * Provides standard CRUD operations and custom query methods for accessing status data.
 */
@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
    /**
     * Finds a status by its name.
     *
     * @param nome The name of the status to find.
     * @return An {@link Optional} containing the status if found, or empty otherwise.
     */
    Optional<Status> findByNome(String nome);
}
