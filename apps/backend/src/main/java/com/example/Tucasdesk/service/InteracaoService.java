package com.example.Tucasdesk.service;

import com.example.Tucasdesk.exception.ResourceNotFoundException;
import com.example.Tucasdesk.messaging.ChamadoMessagingService;
import com.example.Tucasdesk.model.Chamado;
import com.example.Tucasdesk.model.Interacao;
import com.example.Tucasdesk.repository.ChamadoRepository;
import com.example.Tucasdesk.repository.InteracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing interactions.
 */
@Service
public class InteracaoService {

    @Autowired
    private InteracaoRepository interacaoRepository;

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Autowired
    private ChamadoMessagingService chamadoMessagingService;

    /**
     * Creates a new interaction for a given ticket.
     *
     * @param ticketId       The ID of the ticket to add the interaction to.
     * @param newInteraction The interaction to be created.
     * @return The saved interaction.
     * @throws ResourceNotFoundException if the ticket is not found.
     */
    public Interacao createInteraction(Integer ticketId, Interacao newInteraction) {
        Chamado ticket = chamadoRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));
        newInteraction.setChamado(ticket);

        Interacao savedInteraction = interacaoRepository.save(newInteraction);

        chamadoMessagingService.publishChamadoInteracaoAddedEvent(ticket, savedInteraction);

        return savedInteraction;
    }

    /**
     * Retrieves all interactions for a specific ticket.
     *
     * @param ticketId The ID of the ticket.
     * @return A list of interactions for the specified ticket.
     */
    public List<Interacao> getInteractionsByTicketId(Integer ticketId) {
        return interacaoRepository.findByChamadoId(ticketId);
    }
}
