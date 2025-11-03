package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.dtos.LoginDTO;
import com.example.Tucasdesk.dtos.LoginResponseDTO;
import com.example.Tucasdesk.repository.UsuarioRepository;
import com.example.Tucasdesk.security.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.example.Tucasdesk.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication requests, such as user login.
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    /**
     * Authenticates a user based on the provided credentials.
     *
     * @param loginDTO A {@link LoginDTO} containing the user's email and password.
     * @return A {@link ResponseEntity} with a {@link LoginResponseDTO}.
     *         On success, it returns a JWT and a success message.
     *         On failure, it returns an appropriate error message and a 401 status code.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        return usuarioRepository.findByEmail(loginDTO.getEmail())
                .map(usuario -> {
                    if(passwordEncoder.matches(loginDTO.getSenha(), usuario.getSenha())) {
                        String token = tokenService.generateToken(usuario);
                        return ResponseEntity.ok(new LoginResponseDTO(token, usuario, "Login bem-sucedido"));
                    } else {
                        return ResponseEntity.status(401)
                                .body(new LoginResponseDTO(null, null, "Senha incorreta"));
                    }
                })
                .orElse(ResponseEntity.status(401)
                        .body(new LoginResponseDTO(null, null, "Usuário não encontrado")));
    }

    /**
     * Retrieves the currently authenticated user's data.
     *
     * @param authentication The {@link Authentication} object from the security context.
     * @return The currently logged-in {@link Usuario}.
     */
    @GetMapping("/me")
    public ResponseEntity<Usuario> me(Authentication authentication) {
        Usuario currentUser = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }
}
