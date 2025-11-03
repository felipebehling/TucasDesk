package com.example.Tucasdesk.controller;

import org.springframework.web.bind.annotation.*;

import com.example.Tucasdesk.model.Categoria;
import com.example.Tucasdesk.repository.CategoriaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * Controller for handling category-related requests.
 */
@CrossOrigin(origins = "${app.cors.allowed-origins}")
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository CategoriaRepository;

    /**
     * Retrieves a list of all categories.
     *
     * @return A list of all {@link Categoria} objects.
     */
    @GetMapping
    public List<Categoria> listarTodos() {
        return CategoriaRepository.findAll();
    }

    /**
     * Creates a new category.
     *
     * @param Categoria The {@link Categoria} object to create, based on the request body.
     * @return The created {@link Categoria} object.
     */
    @PostMapping
    public Categoria criar(@RequestBody Categoria Categoria) {
        return CategoriaRepository.save(Categoria);
    }
}
