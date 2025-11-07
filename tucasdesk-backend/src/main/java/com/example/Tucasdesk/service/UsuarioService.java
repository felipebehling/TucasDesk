package com.example.Tucasdesk.service;

import com.example.Tucasdesk.dtos.AuthenticatedUserDTO;
import com.example.Tucasdesk.dtos.RegisterRequest;
import com.example.Tucasdesk.dtos.UsuarioResponseDTO;
import com.example.Tucasdesk.dtos.UsuarioUpdateRequest;
import com.example.Tucasdesk.mappers.UsuarioMapper;
import com.example.Tucasdesk.model.Usuario;
import com.example.Tucasdesk.repository.UsuarioRepository;
import com.example.Tucasdesk.security.CognitoService;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Service responsible for operations related to the {@link Usuario} entity.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CognitoService cognitoService;

    private static final Pattern STRONG_PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\"\\-=`~{}\\[\\]:;'<>?,./]).{8,}$"
    );

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, CognitoService cognitoService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.cognitoService = cognitoService;
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
        String rawPassword = usuario.getSenha();
        cognitoService.registerUser(usuario.getEmail(), rawPassword);
        usuario.setSenha(passwordEncoder.encode(rawPassword));
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

    /**
     * Retrieves the authenticated user's data by id.
     *
     * @param idUsuario the identifier of the authenticated user.
     * @return an {@link AuthenticatedUserDTO} representing the current user.
     */
    public AuthenticatedUserDTO obterUsuarioAutenticado(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
        return UsuarioMapper.toAuthenticatedUserDTO(usuario);
    }

    /**
     * Updates the authenticated user's profile data and optional password.
     *
     * @param idUsuario the identifier of the authenticated user.
     * @param updateRequest the payload containing updated information.
     * @return an {@link AuthenticatedUserDTO} with the persisted data.
     */
    public AuthenticatedUserDTO atualizarUsuarioAutenticado(Integer idUsuario, UsuarioUpdateRequest updateRequest) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        String nomeAtualizado = updateRequest.getNome() != null ? updateRequest.getNome().trim() : "";
        String emailAtualizado = updateRequest.getEmail() != null ? updateRequest.getEmail().trim() : "";

        if (nomeAtualizado.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um nome válido.");
        }

        if (emailAtualizado.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um e-mail válido.");
        }

        usuarioRepository.findByEmail(emailAtualizado)
                .filter(existing -> !existing.getIdUsuario().equals(usuario.getIdUsuario()))
                .ifPresent(existing -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário cadastrado com este e-mail.");
                });

        usuario.setNome(nomeAtualizado);
        usuario.setEmail(emailAtualizado);

        boolean alterarSenha = StringUtils.hasText(updateRequest.getNovaSenha())
                || StringUtils.hasText(updateRequest.getConfirmacaoSenha())
                || StringUtils.hasText(updateRequest.getSenhaAtual());

        if (alterarSenha) {
            validarAlteracaoSenha(usuario, updateRequest);
            cognitoService.updatePassword(usuario.getEmail(), updateRequest.getNovaSenha());
            usuario.setSenha(passwordEncoder.encode(updateRequest.getNovaSenha()));
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return UsuarioMapper.toAuthenticatedUserDTO(usuarioAtualizado);
    }

    /**
     * Attempts to locate a user by e-mail.
     *
     * @param email the e-mail address to search for.
     * @return an optional containing the user when present.
     */
    public Optional<Usuario> buscarUsuarioPorEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return usuarioRepository.findByEmail(email);
    }

    private void validarAlteracaoSenha(Usuario usuario, UsuarioUpdateRequest updateRequest) {
        String senhaAtual = updateRequest.getSenhaAtual();
        String novaSenha = updateRequest.getNovaSenha();
        String confirmacaoSenha = updateRequest.getConfirmacaoSenha();

        if (!StringUtils.hasText(senhaAtual)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe sua senha atual para alterá-la.");
        }

        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha atual informada está incorreta.");
        }

        if (!StringUtils.hasText(novaSenha)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a nova senha.");
        }

        if (!StringUtils.hasText(confirmacaoSenha)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Confirme a nova senha.");
        }

        if (!novaSenha.equals(confirmacaoSenha)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A confirmação da nova senha não confere.");
        }

        if (!isStrongPassword(novaSenha)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A nova senha deve conter letras maiúsculas, minúsculas, números e caracteres especiais.");
        }
    }

    private boolean isStrongPassword(String senha) {
        return senha != null && STRONG_PASSWORD_PATTERN.matcher(senha).matches();
    }
}
