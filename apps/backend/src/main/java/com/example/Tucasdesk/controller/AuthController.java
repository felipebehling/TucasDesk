package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.dtos.AuthenticatedUserDTO;
import com.example.Tucasdesk.dtos.ErrorResponseDTO;
import com.example.Tucasdesk.mappers.UsuarioMapper;
import com.example.Tucasdesk.model.Usuario;
import com.example.Tucasdesk.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication requests, such as user login.
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Retrieves the currently authenticated user's data.
     *
     * @param authentication The {@link Authentication} object from the security context.
     * @return The DTO representing the currently logged-in user or an error response.
     */
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("event=auth_me status=unauthenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponseDTO("Usuário não autenticado.", HttpStatus.UNAUTHORIZED.name()));
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Usuario) {
            Usuario usuario = (Usuario) principal;
            AuthenticatedUserDTO usuarioDTO = UsuarioMapper.toAuthenticatedUserDTO(usuario);
            return ResponseEntity.ok(usuarioDTO);
        }

        String username = null;
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            username = userDetails.getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        }

        if (username != null) {
            return usuarioRepository.findByEmail(username)
                    .map(UsuarioMapper::toAuthenticatedUserDTO)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new ErrorResponseDTO("Usuário não encontrado.", HttpStatus.UNAUTHORIZED.name())));
        }

        log.warn("event=auth_me status=user_resolution_failed");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO("Usuário não autenticado.", HttpStatus.UNAUTHORIZED.name()));
    }
}
