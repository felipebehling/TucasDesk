package com.example.Tucasdesk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Tucasdesk.model.Chamado;

/**
 * Repository interface for {@link Chamado} entities.
 * Provides standard CRUD operations and custom query methods for accessing ticket data.
 */
@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Integer> {
    /**
     * Finds a ticket by its title.
     *
     * @param titulo The title of the ticket to find.
     * @return An {@link Optional} containing the ticket if found, or empty otherwise.
     */
    Optional<Chamado> findByTitulo(String titulo);
}
