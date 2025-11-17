package com.example.Tucasdesk.service;

import com.example.Tucasdesk.config.PasswordResetProperties;
import com.example.Tucasdesk.dtos.PasswordResetConfirmationDTO;
import com.example.Tucasdesk.dtos.PasswordResetRequestDTO;
import com.example.Tucasdesk.model.PasswordResetToken;
import com.example.Tucasdesk.model.Usuario;
import com.example.Tucasdesk.repository.PasswordResetTokenRepository;
import com.example.Tucasdesk.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Coordinates the password reset flow and persists reset tokens.
 */
@Service
public class PasswordResetService {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetService.class);

    private static final Pattern STRONG_PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\"\\-=`~{}\\[\\]:;'<>?,./]).{8,}$"
    );

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetEmailService passwordResetEmailService;
    private final PasswordResetProperties passwordResetProperties;

    public PasswordResetService(UsuarioRepository usuarioRepository,
                                PasswordResetTokenRepository passwordResetTokenRepository,
                                PasswordEncoder passwordEncoder,
                                PasswordResetEmailService passwordResetEmailService,
                                PasswordResetProperties passwordResetProperties) {
        this.usuarioRepository = usuarioRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetEmailService = passwordResetEmailService;
        this.passwordResetProperties = passwordResetProperties;
    }

    @Transactional
    public void requestReset(PasswordResetRequestDTO requestDTO) {
        String email = normalizeEmail(requestDTO.getEmail());
        if (!StringUtils.hasText(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um e-mail válido.");
        }

        passwordResetTokenRepository.deleteAllByExpiresAtBefore(Instant.now());

        usuarioRepository.findByEmail(email).ifPresentOrElse(usuario -> {
            log.info("event=password_reset_request status=found userId={} email={}",
                    usuario.getIdUsuario(), usuario.getEmail());
            passwordResetTokenRepository.deleteAllByUsuario(usuario);
            PasswordResetToken token = buildToken(usuario);
            passwordResetTokenRepository.save(token);
            passwordResetEmailService.sendPasswordResetEmail(usuario.getEmail(), token.getToken(), token.getExpiresAt());
        }, () -> log.info("event=password_reset_request status=user_not_found email={}", email));
    }

    @Transactional
    public void resetPassword(PasswordResetConfirmationDTO confirmationDTO) {
        if (!confirmationDTO.hasMatchingPasswords()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A confirmação da senha deve coincidir com a senha informada.");
        }
        if (!isStrongPassword(confirmationDTO.getNovaSenha())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A senha deve conter letras maiúsculas, minúsculas, números e caracteres especiais.");
        }

        PasswordResetToken token = passwordResetTokenRepository.findByToken(confirmationDTO.getToken())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Token inválido ou expirado."));

        Instant now = Instant.now();
        if (token.isUsed() || token.isExpired(now)) {
            passwordResetTokenRepository.delete(token);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido ou expirado.");
        }

        Usuario usuario = token.getUsuario();
        usuario.setSenha(passwordEncoder.encode(confirmationDTO.getNovaSenha()));
        usuarioRepository.save(usuario);

        token.setUsed(true);
        token.setExpiresAt(now);
        passwordResetTokenRepository.save(token);
        log.info("event=password_reset_completed userId={}", usuario.getIdUsuario());
    }

    private PasswordResetToken buildToken(Usuario usuario) {
        PasswordResetToken token = new PasswordResetToken();
        token.setUsuario(usuario);
        token.setToken(generateToken());
        Instant now = Instant.now();
        token.setCreatedAt(now);
        Duration ttl = passwordResetProperties.getTokenTtl();
        Duration effectiveTtl = ttl != null && !ttl.isNegative() ? ttl : Duration.ofMinutes(30);
        token.setExpiresAt(now.plus(effectiveTtl));
        token.setUsed(false);
        return token;
    }

    private String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private boolean isStrongPassword(String password) {
        return password != null && STRONG_PASSWORD_PATTERN.matcher(password).matches();
    }

    private String normalizeEmail(String email) {
        if (email == null) {
            return null;
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
