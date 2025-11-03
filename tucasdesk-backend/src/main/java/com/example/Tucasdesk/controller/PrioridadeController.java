package com.example.Tucasdesk.controller;

import org.springframework.web.bind.annotation.*;

import com.example.Tucasdesk.model.Prioridade;
import com.example.Tucasdesk.repository.PrioridadeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * Controller for handling priority-related requests.
 */
@CrossOrigin(origins = "${app.cors.allowed-origins}")
@RestController
@RequestMapping("/api/prioridades")
public class PrioridadeController {

    @Autowired
    private PrioridadeRepository PrioridadeRepository;

    /**
     * Retrieves a list of all priorities.
     *
     * @return A list of all {@link Prioridade} objects.
     */
    @GetMapping
    public List<Prioridade> listarTodos() {
        return PrioridadeRepository.findAll();
    }

    /**
     * Creates a new priority.
     *
     * @param Prioridade The {@link Prioridade} object to create, based on the request body.
     * @return The created {@link Prioridade} object.
     */
    @PostMapping
    public Prioridade criar(@RequestBody Prioridade Prioridade) {
        return PrioridadeRepository.save(Prioridade);
    }
}
