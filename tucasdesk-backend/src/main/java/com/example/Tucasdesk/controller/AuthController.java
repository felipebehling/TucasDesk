package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.dtos.LoginDTO;
import com.example.Tucasdesk.dtos.LoginResponseDTO;
import com.example.Tucasdesk.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        return usuarioRepository.findByNome(loginDTO.getNome())
                .map(usuario -> {
                    if(usuario.getSenha().equals(loginDTO.getSenha())) {
                        // Aqui você pode gerar JWT depois
                        return ResponseEntity.ok(new LoginResponseDTO("fake-jwt-token", usuario.getNome(), "Login bem-sucedido"));
                    } else {
                        return ResponseEntity.status(401)
                                .body(new LoginResponseDTO(null, loginDTO.getNome(), "Senha incorreta"));
                    }
                })
                .orElse(ResponseEntity.status(401)
                        .body(new LoginResponseDTO(null, loginDTO.getNome(), "Usuário não encontrado")));
    }
}
