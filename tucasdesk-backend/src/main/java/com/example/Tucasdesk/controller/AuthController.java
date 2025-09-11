package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.dtos.LoginDTO;
import com.example.Tucasdesk.dtos.LoginResponseDTO;
import com.example.Tucasdesk.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication requests, such as user login.
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
                        // TODO: Implement actual JWT generation
                        return ResponseEntity.ok(new LoginResponseDTO("fake-jwt-token", usuario.getNome(), "Login bem-sucedido"));
                    } else {
                        return ResponseEntity.status(401)
                                .body(new LoginResponseDTO(null, loginDTO.getEmail(), "Senha incorreta"));
                    }
                })
                .orElse(ResponseEntity.status(401)
                        .body(new LoginResponseDTO(null, loginDTO.getEmail(), "Usuário não encontrado")));
    }
}
