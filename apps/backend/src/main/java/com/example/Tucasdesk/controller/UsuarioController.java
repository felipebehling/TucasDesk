package com.example.Tucasdesk.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.Tucasdesk.dtos.AuthenticatedUserDTO;
import com.example.Tucasdesk.dtos.ErrorResponseDTO;
import com.example.Tucasdesk.dtos.UsuarioResponseDTO;
import com.example.Tucasdesk.dtos.UsuarioUpdateRequest;
import com.example.Tucasdesk.model.Usuario;
import com.example.Tucasdesk.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.Valid;

import org.springframework.web.server.ResponseStatusException;

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
     * Retrieves the profile information of the currently authenticated user.
     *
     * @param authentication the current authentication context.
     * @return the authenticated user's information or an error payload.
     */
    @GetMapping("/me")
    public ResponseEntity<?> obterPerfil(Authentication authentication) {
        Usuario usuarioAutenticado = extrairUsuarioAutenticado(authentication);
        if (usuarioAutenticado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponseDTO("Usuário não autenticado.", HttpStatus.UNAUTHORIZED.name()));
        }

        try {
            AuthenticatedUserDTO dto = usuarioService.obterUsuarioAutenticado(usuarioAutenticado.getIdUsuario());
            return ResponseEntity.ok(dto);
        } catch (ResponseStatusException ex) {
            HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
            if (status == null) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            return ResponseEntity.status(ex.getStatusCode())
                    .body(new ErrorResponseDTO(mensagemErro(ex, "Não foi possível carregar os dados do usuário."),
                            status.name()));
        }
    }

    /**
     * Updates the authenticated user's profile information.
     *
     * @param authentication the authentication context.
     * @param updateRequest the payload containing updated user data.
     * @param bindingResult validation state for the request payload.
     * @return the updated user data or an error payload.
     */
    @PutMapping("/me")
    public ResponseEntity<?> atualizarPerfil(
            Authentication authentication,
            @Valid @RequestBody UsuarioUpdateRequest updateRequest,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Dados inválidos.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDTO(errorMessage, HttpStatus.BAD_REQUEST.name()));
        }

        Usuario usuarioAutenticado = extrairUsuarioAutenticado(authentication);
        if (usuarioAutenticado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponseDTO("Usuário não autenticado.", HttpStatus.UNAUTHORIZED.name()));
        }

        try {
            AuthenticatedUserDTO dto = usuarioService.atualizarUsuarioAutenticado(
                    usuarioAutenticado.getIdUsuario(), updateRequest);
            return ResponseEntity.ok(dto);
        } catch (ResponseStatusException ex) {
            HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
            if (status == null) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            return ResponseEntity.status(ex.getStatusCode())
                    .body(new ErrorResponseDTO(mensagemErro(ex, "Não foi possível atualizar os dados do usuário."),
                            status.name()));
        }
    }

    /**
     * Creates a new user. The user's password will be encoded before being saved.
     *
     * @param usuario The {@link Usuario} object to create, based on the request body.
     * @param bindingResult Validation result for the request body.
     * @return The created user represented as a {@link UsuarioResponseDTO}.
     */
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody Usuario usuario, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Dados inválidos.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDTO(errorMessage, HttpStatus.BAD_REQUEST.name()));
        }
        UsuarioResponseDTO novoUsuario = usuarioService.criarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    private Usuario extrairUsuarioAutenticado(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Usuario) {
            return (Usuario) principal;
        }

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return usuarioService.buscarUsuarioPorEmail(userDetails.getUsername()).orElse(null);
        }

        if (principal instanceof String) {
            String username = (String) principal;
            return usuarioService.buscarUsuarioPorEmail(username).orElse(null);
        }

        return null;
    }

    private String mensagemErro(ResponseStatusException exception, String fallback) {
        String reason = exception.getReason();
        return reason != null ? reason : fallback;
    }
}
