package com.example.Tucasdesk.controller;

import org.springframework.web.bind.annotation.*;

import com.example.Tucasdesk.model.Chamado;
import com.example.Tucasdesk.repository.ChamadoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * Controller for handling ticket-related requests.
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/chamados")
public class ChamadoController {

    @Autowired
    private ChamadoRepository chamadoRepository;

    /**
     * Retrieves a list of all tickets.
     *
     * @return A list of all {@link Chamado} objects.
     */
    @GetMapping
    public List<Chamado> listarTodos() {
        return chamadoRepository.findAll();
    }

    /**
     * Creates a new ticket.
     *
     * @param chamado The {@link Chamado} object to create, based on the request body.
     * @return The created {@link Chamado} object.
     */
    @PostMapping
    public Chamado criar(@RequestBody Chamado chamado) {
        return chamadoRepository.save(chamado);
    }
}
