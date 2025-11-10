package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.dtos.LookupRequestDTO;
import com.example.Tucasdesk.dtos.LookupResponseDTO;
import com.example.Tucasdesk.service.LookupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling profile-related requests.
 */
@CrossOrigin(origins = "${app.cors.allowed-origins}")
@RestController
@RequestMapping("/api/perfis")
public class PerfilController {

    private final LookupService lookupService;

    public PerfilController(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    /**
     * Retrieves a list of all profiles.
     *
     * @return A list of all {@link LookupResponseDTO} objects.
     */
    @GetMapping
    public List<LookupResponseDTO> listarTodos() {
        return lookupService.listarPerfis();
    }

    /**
     * Retrieves a single profile by its identifier.
     *
     * @param id the profile identifier.
     * @return the profile payload.
     */
    @GetMapping("/{id}")
    public LookupResponseDTO buscarPorId(@PathVariable Integer id) {
        return lookupService.buscarPerfil(id);
    }

    /**
     * Creates a new profile.
     *
     * @param request The payload to create the profile.
     * @return The created profile.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LookupResponseDTO criar(@Valid @RequestBody LookupRequestDTO request) {
        return lookupService.criarPerfil(request);
    }

    /**
     * Updates the name of a profile.
     *
     * @param id      the profile identifier.
     * @param request the payload containing the updated information.
     * @return the updated profile payload.
     */
    @PutMapping("/{id}")
    public LookupResponseDTO atualizar(@PathVariable Integer id, @Valid @RequestBody LookupRequestDTO request) {
        return lookupService.atualizarPerfil(id, request);
    }

    /**
     * Removes a profile by its identifier.
     *
     * @param id the profile identifier.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Integer id) {
        lookupService.removerPerfil(id);
    }
}
