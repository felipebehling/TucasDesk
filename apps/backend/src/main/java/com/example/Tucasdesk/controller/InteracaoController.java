package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.model.Interacao;
import com.example.Tucasdesk.service.InteracaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/chamados/{chamadoId}/interacoes")
public class InteracaoController {

    @Autowired
    private InteracaoService interacaoService;

    @PostMapping
    public ResponseEntity<Interacao> createInteraction(@PathVariable Integer chamadoId, @RequestBody Interacao interacao) {
        Interacao createdInteraction = interacaoService.createInteraction(chamadoId, interacao);
        URI location = URI.create(String.format("/api/chamados/%d/interacoes/%d", chamadoId, createdInteraction.getIdInteracao()));
        return ResponseEntity.created(location).body(createdInteraction);
    }

    @GetMapping
    public ResponseEntity<List<Interacao>> getInteractionsByTicketId(@PathVariable Integer chamadoId) {
        List<Interacao> interactions = interacaoService.getInteractionsByTicketId(chamadoId);
        return ResponseEntity.ok(interactions);
    }
}
