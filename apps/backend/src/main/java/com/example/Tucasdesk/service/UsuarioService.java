package com.example.Tucasdesk.service;

import com.example.Tucasdesk.model.Perfil;
import com.example.Tucasdesk.model.Usuario;
import com.example.Tucasdesk.repository.UsuarioRepository;
import com.example.Tucasdesk.dtos.UsuarioResponseDTO;
import com.example.Tucasdesk.mappers.UsuarioMapper;
import com.example.Tucasdesk.repository.PerfilRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private static final String DEFAULT_PROFILE_NAME = "Usuário";

    public UsuarioService(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
    }

    @Transactional
    public Usuario findOrCreateFromJwt(Jwt jwt) {
        String cognitoId = jwt.getSubject();

        return usuarioRepository.findByCognitoId(cognitoId)
            .orElseGet(() -> {
                Usuario novoUsuario = new Usuario();
                novoUsuario.setCognitoId(cognitoId);
                novoUsuario.setEmail(jwt.getClaimAsString("email"));
                novoUsuario.setNome(jwt.getClaimAsString("name"));
                novoUsuario.setAtivo(true);
                novoUsuario.setDataCriacao(LocalDateTime.now());

                Perfil perfilPadrao = perfilRepository.findByNomeIgnoreCase(DEFAULT_PROFILE_NAME)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Perfil padrão 'Usuário' não encontrado."));
                novoUsuario.setPerfil(perfilPadrao);

                return usuarioRepository.save(novoUsuario);
            });
    }

    public List<UsuarioResponseDTO> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return UsuarioMapper.toUsuarioResponseDTOList(usuarios);
    }
}
