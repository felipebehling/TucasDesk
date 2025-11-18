package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.model.Interacao;
import com.example.Tucasdesk.service.InteracaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller for handling interactions within a ticket.
 */
@RestController
@RequestMapping("/api/chamados/{chamadoId}/interacoes")
public class InteracaoController {

    @Autowired
    private InteracaoService interacaoService;

    /**
     * Creates a new interaction for a given ticket.
     *
     * @param chamadoId The ID of the ticket to add the interaction to.
     * @param interacao The interaction object to be created.
     * @return A {@link ResponseEntity} with the created interaction and a 201 Created status.
     */
    @PostMapping
    public ResponseEntity<Interacao> createInteraction(@PathVariable Integer chamadoId, @RequestBody Interacao interacao) {
        Interacao createdInteraction = interacaoService.createInteraction(chamadoId, interacao);
        URI location = URI.create(String.format("/api/chamados/%d/interacoes/%d", chamadoId, createdInteraction.getIdInteracao()));
        return ResponseEntity.created(location).body(createdInteraction);
    }

    /**
     * Retrieves all interactions for a specific ticket.
     *
     * @param chamadoId The ID of the ticket.
     * @return A {@link ResponseEntity} containing a list of interactions and a 200 OK status.
     */
    @GetMapping
    public ResponseEntity<List<Interacao>> getInteractionsByTicketId(@PathVariable Integer chamadoId) {
        List<Interacao> interactions = interacaoService.getInteractionsByTicketId(chamadoId);
        return ResponseEntity.ok(interactions);
    }
}
