package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.dtos.LookupRequestDTO;
import com.example.Tucasdesk.dtos.LookupResponseDTO;
import com.example.Tucasdesk.service.LookupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling status-related requests.
 */
@CrossOrigin(origins = "${app.cors.allowed-origins}")
@RestController
@RequestMapping("/api/status")
public class StatusController {

    private final LookupService lookupService;

    /**
     * Constructs a new {@code StatusController} with the specified lookup service.
     *
     * @param lookupService The service responsible for handling lookup-related operations.
     */
    public StatusController(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    /**
     * Retrieves a list of all statuses.
     *
     * @return A list of all {@link LookupResponseDTO} objects.
     */
    @GetMapping
    public List<LookupResponseDTO> listarTodos() {
        return lookupService.listarStatus();
    }

    /**
     * Retrieves a single status by its identifier.
     *
     * @param id the status identifier.
     * @return the status payload.
     */
    @GetMapping("/{id}")
    public LookupResponseDTO buscarPorId(@PathVariable Integer id) {
        return lookupService.buscarStatus(id);
    }

    /**
     * Creates a new status.
     *
     * @param request The payload to create the status.
     * @return The created status.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LookupResponseDTO criar(@Valid @RequestBody LookupRequestDTO request) {
        return lookupService.criarStatus(request);
    }

    /**
     * Updates the name of a status.
     *
     * @param id      the status identifier.
     * @param request the payload containing the updated information.
     * @return the updated status payload.
     */
    @PutMapping("/{id}")
    public LookupResponseDTO atualizar(@PathVariable Integer id, @Valid @RequestBody LookupRequestDTO request) {
        return lookupService.atualizarStatus(id, request);
    }

    /**
     * Removes a status by its identifier.
     *
     * @param id the status identifier.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Integer id) {
        lookupService.removerStatus(id);
    }
}
