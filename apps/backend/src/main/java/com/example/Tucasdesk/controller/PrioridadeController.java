package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.dtos.LookupRequestDTO;
import com.example.Tucasdesk.dtos.LookupResponseDTO;
import com.example.Tucasdesk.service.LookupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling priority-related requests.
 */
@CrossOrigin(origins = "${app.cors.allowed-origins}")
@RestController
@RequestMapping("/api/prioridades")
public class PrioridadeController {

    private final LookupService lookupService;

    public PrioridadeController(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    /**
     * Retrieves a list of all priorities.
     *
     * @return A list of all {@link LookupResponseDTO} objects.
     */
    @GetMapping
    public List<LookupResponseDTO> listarTodos() {
        return lookupService.listarPrioridades();
    }

    /**
     * Retrieves a single priority by its identifier.
     *
     * @param id the priority identifier.
     * @return the priority payload.
     */
    @GetMapping("/{id}")
    public LookupResponseDTO buscarPorId(@PathVariable Integer id) {
        return lookupService.buscarPrioridade(id);
    }

    /**
     * Creates a new priority.
     *
     * @param request The payload to create the priority.
     * @return The created priority.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LookupResponseDTO criar(@Valid @RequestBody LookupRequestDTO request) {
        return lookupService.criarPrioridade(request);
    }

    /**
     * Updates the name of a priority.
     *
     * @param id      the priority identifier.
     * @param request the payload containing the updated information.
     * @return the updated priority payload.
     */
    @PutMapping("/{id}")
    public LookupResponseDTO atualizar(@PathVariable Integer id, @Valid @RequestBody LookupRequestDTO request) {
        return lookupService.atualizarPrioridade(id, request);
    }

    /**
     * Removes a priority by its identifier.
     *
     * @param id the priority identifier.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Integer id) {
        lookupService.removerPrioridade(id);
    }
}
