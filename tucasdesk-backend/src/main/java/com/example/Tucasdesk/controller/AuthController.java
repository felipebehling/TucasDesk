package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.dtos.AuthenticatedUserDTO;
import com.example.Tucasdesk.dtos.ErrorResponseDTO;
import com.example.Tucasdesk.dtos.LoginDTO;
import com.example.Tucasdesk.dtos.LoginResponseDTO;
import com.example.Tucasdesk.dtos.RegisterRequest;
import com.example.Tucasdesk.dtos.UsuarioResponseDTO;
import com.example.Tucasdesk.mappers.UsuarioMapper;
import com.example.Tucasdesk.model.Usuario;
import com.example.Tucasdesk.repository.UsuarioRepository;
import com.example.Tucasdesk.security.CognitoAuthenticationResult;
import com.example.Tucasdesk.security.CognitoService;
import com.example.Tucasdesk.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.regex.Pattern;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CognitoService cognitoService;

    @Autowired
    private UsuarioService usuarioService;

    private static final Pattern STRONG_PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\"\\-=`~{}\\[\\]:;'<>?,./]).{8,}$"
    );

    /**
     * Registers a new user after validating the provided payload.
     *
     * @param registerRequest The payload containing the new user's information.
     * @param bindingResult   Validation result for the request body.
     * @return A {@link UsuarioResponseDTO} on success or an error payload describing the issue.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .findFirst()
                    .orElse("Dados inválidos para registro.");
            log.warn("event=auth_register status=validation_error email={} message=\"{}\"", registerRequest.getEmail(), errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDTO(errorMessage, HttpStatus.BAD_REQUEST.name()));
        }

        if (!isStrongPassword(registerRequest.getSenha())) {
            log.warn("event=auth_register status=weak_password email={}", registerRequest.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDTO("A senha deve conter letras maiúsculas, minúsculas, números e caracteres especiais.", HttpStatus.BAD_REQUEST.name()));
        }

        if (usuarioRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            log.warn("event=auth_register status=conflict email={}", registerRequest.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponseDTO("Já existe um usuário cadastrado com este e-mail.", HttpStatus.CONFLICT.name()));
        }

        try {
            UsuarioResponseDTO usuarioResponseDTO = usuarioService.criarUsuario(registerRequest);
            log.info("event=auth_register status=success userId={} email={}", usuarioResponseDTO.getIdUsuario(), usuarioResponseDTO.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponseDTO);
        } catch (ResponseStatusException ex) {
            HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
            if (status == null) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            String message = ex.getReason() != null ? ex.getReason() : "Não foi possível registrar o usuário.";
            log.warn("event=auth_register status=error email={} message=\"{}\"", registerRequest.getEmail(), message, ex);
            return ResponseEntity.status(ex.getStatusCode()).body(new ErrorResponseDTO(message, status.name()));
        }
    }

    /**
     * Authenticates a user based on the provided credentials.
     *
     * @param loginDTO A {@link LoginDTO} containing the user's email and password.
     * @return A {@link ResponseEntity} with a {@link LoginResponseDTO} on success or an
     *         error payload with an appropriate HTTP status on failure.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(loginDTO.getEmail());

        if (optionalUsuario.isEmpty()) {
            log.warn("event=auth_login status=user_not_found email={}", loginDTO.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponseDTO("Usuário ou senha inválidos.", HttpStatus.UNAUTHORIZED.name()));
        }

        Usuario usuario = optionalUsuario.get();
        if (!passwordEncoder.matches(loginDTO.getSenha(), usuario.getSenha())) {
            log.warn("event=auth_login status=invalid_password userId={} email={}", usuario.getIdUsuario(), usuario.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponseDTO("Usuário ou senha inválidos.", HttpStatus.UNAUTHORIZED.name()));
        }

        try {
            CognitoAuthenticationResult tokens = cognitoService.authenticate(usuario.getEmail(), loginDTO.getSenha());
            AuthenticatedUserDTO usuarioDTO = UsuarioMapper.toAuthenticatedUserDTO(usuario);
            LoginResponseDTO responseDTO = new LoginResponseDTO(tokens.idToken(), tokens.accessToken(), tokens.refreshToken(), usuarioDTO);
            log.info("event=auth_login status=success userId={} email={}", usuario.getIdUsuario(), usuario.getEmail());
            return ResponseEntity.ok(responseDTO);
        } catch (ResponseStatusException ex) {
            HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
            if (status == null) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            String message = ex.getReason() != null ? ex.getReason() : "Não foi possível autenticar o usuário.";
            log.warn("event=auth_login status=error userId={} email={} message=\"{}\"", usuario.getIdUsuario(), usuario.getEmail(), message, ex);
            return ResponseEntity.status(ex.getStatusCode()).body(new ErrorResponseDTO(message, status.name()));
        }
    }

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
        if (principal instanceof Usuario usuario) {
            AuthenticatedUserDTO usuarioDTO = UsuarioMapper.toAuthenticatedUserDTO(usuario);
            return ResponseEntity.ok(usuarioDTO);
        }

        String username = null;
        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else if (principal instanceof String principalString) {
            username = principalString;
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

    private boolean isStrongPassword(String senha) {
        return senha != null && STRONG_PASSWORD_PATTERN.matcher(senha).matches();
    }
}
