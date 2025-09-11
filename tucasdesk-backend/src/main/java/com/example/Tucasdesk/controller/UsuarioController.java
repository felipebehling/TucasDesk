package com.example.Tucasdesk.controller;

import org.springframework.web.bind.annotation.*;

import com.example.Tucasdesk.dtos.UsuarioResponseDTO;
import com.example.Tucasdesk.model.Usuario;
import com.example.Tucasdesk.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for handling user-related requests.
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Retrieves a list of all users.
     *
     * @return A list of all {@link Usuario} objects.
     */
    @GetMapping
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuario -> new UsuarioResponseDTO(
                        usuario.getIdUsuario(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getPerfil(),
                        usuario.getDataCriacao(),
                        usuario.getAtivo()))
                .collect(Collectors.toList());
    }

    /**
     * Creates a new user.
     * The user's password will be encoded before being saved.
     *
     * @param usuario The {@link Usuario} object to create, based on the request body.
     * @return The created {@link Usuario} object.
     */
    @PostMapping
    public Usuario criar(@RequestBody Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }
}
