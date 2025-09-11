package com.example.Tucasdesk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Tucasdesk.model.Prioridade;

/**
 * Repository interface for {@link Prioridade} entities.
 * Provides standard CRUD operations and custom query methods for accessing priority data.
 */
@Repository
public interface PrioridadeRepository extends JpaRepository<Prioridade, Integer> {
    /**
     * Finds a priority by its name.
     *
     * @param nome The name of the priority to find.
     * @return An {@link Optional} containing the priority if found, or empty otherwise.
     */
    Optional<Prioridade> findByNome(String nome);
}
