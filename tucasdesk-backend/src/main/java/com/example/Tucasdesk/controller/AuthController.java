package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.dtos.LoginDTO;
import com.example.Tucasdesk.dtos.LoginResponseDTO;
import com.example.Tucasdesk.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        return usuarioRepository.findByEmail(loginDTO.getEmail())
                .map(usuario -> {
                    if(passwordEncoder.matches(loginDTO.getSenha(), usuario.getSenha())) {
                        // Aqui você pode gerar JWT depois
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
