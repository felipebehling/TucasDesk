package com.example.Tucasdesk.controller;

import org.springframework.web.bind.annotation.*;

import com.example.Tucasdesk.dtos.UsuarioResponseDTO;
import com.example.Tucasdesk.model.Usuario;
import com.example.Tucasdesk.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Controller for handling user-related requests.
 */
@CrossOrigin(origins = "${app.cors.allowed-origins}")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Retrieves a list of all users.
     *
     * @return A list of {@link UsuarioResponseDTO} objects containing non-sensitive user data.
     */
    @GetMapping
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioService.listarUsuarios();
    }

    /**
     * Creates a new user. The user's password will be encoded before being saved.
     *
     * @param usuario The {@link Usuario} object to create, based on the request body.
     * @return The created user represented as a {@link UsuarioResponseDTO}.
     */
    @PostMapping
    public UsuarioResponseDTO criar(@RequestBody Usuario usuario) {
        return usuarioService.criarUsuario(usuario);
    }
}
