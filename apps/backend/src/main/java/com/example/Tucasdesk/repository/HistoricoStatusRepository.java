package com.example.Tucasdesk.repository;

import com.example.Tucasdesk.model.Chamado;
import com.example.Tucasdesk.model.HistoricoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface responsible for accessing the status history of tickets.
 */
@Repository
public interface HistoricoStatusRepository extends JpaRepository<HistoricoStatus, Integer> {

    /**
     * Retrieves all status history entries for the informed ticket ordered by creation date.
     *
     * @param chamado the ticket entity.
     * @return an ordered list of status history entries.
     */
    List<HistoricoStatus> findByChamadoOrderByDataRegistroAsc(Chamado chamado);
}
