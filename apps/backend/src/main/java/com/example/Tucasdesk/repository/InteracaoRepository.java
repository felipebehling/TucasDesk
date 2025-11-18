package com.example.Tucasdesk.repository;

import com.example.Tucasdesk.model.Chamado;
import com.example.Tucasdesk.model.Interacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Interacao} entities.
 * Provides standard CRUD operations for accessing interaction data.
 */
@Repository
public interface InteracaoRepository extends JpaRepository<Interacao, Integer> {

    /**
     * Retrieves all interactions for the informed ticket ordered by their timestamp.
     *
     * @param chamado the ticket entity.
     * @return an ordered list of interactions.
     */
    List<Interacao> findByChamadoOrderByDataInteracaoAsc(Chamado chamado);

    /**
     * Finds all interactions associated with a given ticket ID.
     *
     * @param chamadoId The ID of the ticket.
     * @return A list of interactions for the specified ticket.
     */
    List<Interacao> findByChamadoId(Integer chamadoId);
}
