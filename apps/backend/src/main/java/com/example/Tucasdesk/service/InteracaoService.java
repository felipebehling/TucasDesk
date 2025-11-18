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

@Service
public class InteracaoService {

    @Autowired
    private InteracaoRepository interacaoRepository;

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Autowired
    private ChamadoMessagingService chamadoMessagingService;

    public Interacao createInteraction(Integer ticketId, Interacao newInteraction) {
        Chamado ticket = chamadoRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));
        newInteraction.setChamado(ticket);

        Interacao savedInteraction = interacaoRepository.save(newInteraction);

        chamadoMessagingService.publishChamadoInteracaoAddedEvent(ticket, savedInteraction);

        return savedInteraction;
    }

    public List<Interacao> getInteractionsByTicketId(Integer ticketId) {
        return interacaoRepository.findByChamadoId(ticketId);
    }
}
