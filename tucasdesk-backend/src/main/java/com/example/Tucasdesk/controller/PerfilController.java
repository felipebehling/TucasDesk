package com.example.Tucasdesk.controller;

import org.springframework.web.bind.annotation.*;

import com.example.Tucasdesk.model.Perfil;
import com.example.Tucasdesk.repository.PerfilRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * Controller for handling profile-related requests.
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/perfis")
public class PerfilController {

    @Autowired
    private PerfilRepository PerfilRepository;

    /**
     * Retrieves a list of all profiles.
     *
     * @return A list of all {@link Perfil} objects.
     */
    @GetMapping
    public List<Perfil> listarTodos() {
        return PerfilRepository.findAll();
    }

    /**
     * Creates a new profile.
     *
     * @param Perfil The {@link Perfil} object to create, based on the request body.
     * @return The created {@link Perfil} object.
     */
    @PostMapping
    public Perfil criar(@RequestBody Perfil Perfil) {
        return PerfilRepository.save(Perfil);
    }
}
