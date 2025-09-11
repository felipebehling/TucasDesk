package com.example.Tucasdesk.controller;

import org.springframework.web.bind.annotation.*;

import com.example.Tucasdesk.model.Interacao;
import com.example.Tucasdesk.repository.InteracaoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * Controller for handling interaction-related requests.
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/interacoes")
public class InteracaoController {

    @Autowired
    private InteracaoRepository InteracaoRepository;

    /**
     * Retrieves a list of all interactions.
     *
     * @return A list of all {@link Interacao} objects.
     */
    @GetMapping
    public List<Interacao> listarTodos() {
        return InteracaoRepository.findAll();
    }

    /**
     * Creates a new interaction.
     *
     * @param Interacao The {@link Interacao} object to create, based on the request body.
     * @return The created {@link Interacao} object.
     */
    @PostMapping
    public Interacao criar(@RequestBody Interacao Interacao) {
        return InteracaoRepository.save(Interacao);
    }
}
