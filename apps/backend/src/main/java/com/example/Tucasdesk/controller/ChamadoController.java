package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.dtos.*;
import com.example.Tucasdesk.service.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controller for handling ticket-related requests.
 */
@CrossOrigin(origins = "${app.cors.allowed-origins}")
@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    /**
     * Retrieves a list of all tickets.
     *
     * @return a list of {@link ChamadoResponseDTO} objects.
     */
    @GetMapping
    public List<ChamadoResponseDTO> listarTodos() {
        return chamadoService.listarTodos();
    }

    /**
     * Retrieves the details of a ticket by its identifier.
     *
     * @param id the ticket identifier.
     * @return the ticket details.
     */
    @GetMapping("/{id}")
    public ChamadoResponseDTO buscarPorId(@PathVariable Integer id) {
        return chamadoService.buscarPorId(id);
    }

    /**
     * Creates a new ticket.
     *
     * @param request The request payload used to create the ticket.
     * @return The created ticket.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChamadoResponseDTO criar(@Valid @RequestBody ChamadoRequest request) {
        return chamadoService.criar(request);
    }

    /**
     * Updates an existing ticket replacing mutable information.
     *
     * @param id      the ticket identifier.
     * @param request the payload with the modifications.
     * @return the updated ticket.
     */
    @PutMapping("/{id}")
    public ChamadoResponseDTO atualizar(@PathVariable Integer id, @RequestBody ChamadoUpdateRequest request) {
        return chamadoService.atualizar(id, request);
    }

    /**
     * Partially updates a ticket status.
     *
     * @param id      the ticket identifier.
     * @param request the payload containing the new status.
     * @return the updated ticket.
     */
    @PatchMapping("/{id}/status")
    public ChamadoResponseDTO atualizarStatus(@PathVariable Integer id, @RequestBody ChamadoUpdateRequest request) {
        if (request.getStatusId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O status é obrigatório para atualização parcial.");
        }
        return chamadoService.atualizarStatus(id, request.getStatusId());
    }

    /**
     * Partially updates a ticket priority.
     *
     * @param id      the ticket identifier.
     * @param request the payload containing the new priority.
     * @return the updated ticket.
     */
    @PatchMapping("/{id}/prioridade")
    public ChamadoResponseDTO atualizarPrioridade(@PathVariable Integer id, @RequestBody ChamadoUpdateRequest request) {
        if (request.getPrioridadeId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A prioridade é obrigatória para atualização parcial.");
        }
        return chamadoService.atualizarPrioridade(id, request.getPrioridadeId());
    }

    /**
     * Adds a new interaction to a ticket.
     *
     * @param id      the ticket identifier.
     * @param request the interaction payload.
     * @return the created interaction.
     */
    @PostMapping("/{id}/interacoes")
    @ResponseStatus(HttpStatus.CREATED)
    public InteracaoResponseDTO adicionarInteracao(@PathVariable Integer id, @Valid @RequestBody InteracaoRequest request) {
        return chamadoService.adicionarInteracao(id, request);
    }

    /**
     * Removes an interaction from a ticket.
     *
     * @param id           the ticket identifier.
     * @param interacaoId  the interaction identifier.
     * @param usuarioId    the optional interaction author identifier used to validate the removal.
     */
    @DeleteMapping("/{id}/interacoes/{interacaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerInteracao(@PathVariable Integer id,
                                 @PathVariable Integer interacaoId,
                                 @RequestParam(value = "usuarioId") Integer usuarioId) {
        chamadoService.removerInteracao(id, interacaoId, usuarioId);
    }
}
