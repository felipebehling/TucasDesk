package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.dtos.LookupRequestDTO;
import com.example.Tucasdesk.dtos.LookupResponseDTO;
import com.example.Tucasdesk.service.LookupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling category-related requests.
 */
@CrossOrigin(origins = "${app.cors.allowed-origins}")
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final LookupService lookupService;

    /**
     * Constructs a new {@code CategoriaController} with the specified lookup service.
     *
     * @param lookupService The service responsible for handling lookup-related operations.
     */
    public CategoriaController(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    /**
     * Retrieves a list of all categories.
     *
     * @return A list of all {@link LookupResponseDTO} objects.
     */
    @GetMapping
    public List<LookupResponseDTO> listarTodos() {
        return lookupService.listarCategorias();
    }

    /**
     * Retrieves a single category by its identifier.
     *
     * @param id the category identifier.
     * @return the category payload.
     */
    @GetMapping("/{id}")
    public LookupResponseDTO buscarPorId(@PathVariable Integer id) {
        return lookupService.buscarCategoria(id);
    }

    /**
     * Creates a new category.
     *
     * @param request The payload to create the category.
     * @return The created category.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LookupResponseDTO criar(@Valid @RequestBody LookupRequestDTO request) {
        return lookupService.criarCategoria(request);
    }

    /**
     * Updates the name of a category.
     *
     * @param id      the category identifier.
     * @param request the payload containing the updated information.
     * @return the updated category payload.
     */
    @PutMapping("/{id}")
    public LookupResponseDTO atualizar(@PathVariable Integer id, @Valid @RequestBody LookupRequestDTO request) {
        return lookupService.atualizarCategoria(id, request);
    }

    /**
     * Removes a category by its identifier.
     *
     * @param id the category identifier.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Integer id) {
        lookupService.removerCategoria(id);
    }
}
