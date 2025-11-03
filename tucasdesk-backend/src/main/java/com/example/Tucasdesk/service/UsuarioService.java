package com.example.Tucasdesk.service;

import com.example.Tucasdesk.dtos.RegisterRequest;
import com.example.Tucasdesk.dtos.UsuarioResponseDTO;
import com.example.Tucasdesk.mappers.UsuarioMapper;
import com.example.Tucasdesk.model.Usuario;
import com.example.Tucasdesk.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service responsible for operations related to the {@link Usuario} entity.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new user based on the information provided in a {@link RegisterRequest}.
     *
     * @param registerRequest the registration data.
     * @return a {@link UsuarioResponseDTO} without sensitive data.
     */
    public UsuarioResponseDTO criarUsuario(RegisterRequest registerRequest) {
        Usuario usuario = new Usuario();
        usuario.setNome(registerRequest.getNome());
        usuario.setEmail(registerRequest.getEmail());
        usuario.setSenha(registerRequest.getSenha());
        return criarUsuario(usuario);
    }

    /**
     * Persists a {@link Usuario} applying the necessary transformations and returns a sanitised DTO.
     *
     * @param usuario the user to be created.
     * @return a {@link UsuarioResponseDTO} representing the persisted user without the password hash.
     */
    public UsuarioResponseDTO criarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        if (usuario.getDataCriacao() == null) {
            usuario.setDataCriacao(LocalDateTime.now());
        }
        if (usuario.getAtivo() == null) {
            usuario.setAtivo(true);
        }
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return UsuarioMapper.toUsuarioResponseDTO(usuarioSalvo);
    }

    /**
     * Retrieves all persisted {@link Usuario} entities and converts them into DTOs.
     *
     * @return a list containing non-sensitive data for each user.
     */
    public List<UsuarioResponseDTO> listarUsuarios() {
        return UsuarioMapper.toUsuarioResponseDTOList(usuarioRepository.findAll());
    }
}
