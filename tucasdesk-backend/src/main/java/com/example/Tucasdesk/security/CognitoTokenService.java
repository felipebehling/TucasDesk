package com.example.Tucasdesk.security;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import com.example.Tucasdesk.config.AwsCognitoProperties;
import com.example.Tucasdesk.repository.UsuarioRepository;

/**
 * Service responsible for validating AWS Cognito tokens and mapping them to the
 * application's authentication model.
 */
@Service
public class CognitoTokenService {

    private static final Logger log = LoggerFactory.getLogger(CognitoTokenService.class);

    private final AwsCognitoProperties properties;
    private final UsuarioRepository usuarioRepository;
    private final AtomicReference<JwtDecoder> decoderReference = new AtomicReference<>();

    public CognitoTokenService(AwsCognitoProperties properties, UsuarioRepository usuarioRepository) {
        this.properties = properties;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Attempts to authenticate the current security context using the provided
     * bearer token.
     *
     * @param token the bearer token received in the request.
     */
    public void authenticate(String token) {
        if (!properties.isAuthenticationEnabled()) {
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }

        Optional<Jwt> jwt = decode(token);
        if (jwt.isEmpty()) {
            return;
        }

        Optional.ofNullable(extractUsername(jwt.get()))
                .flatMap(usuarioRepository::findByEmail)
                .ifPresentOrElse(usuario -> {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            usuario, null, usuario.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }, () -> log.warn("event=cognito_authentication status=user_not_found subject={}", jwt.get().getSubject()));
    }

    private Optional<Jwt> decode(String token) {
        try {
            JwtDecoder decoder = resolveDecoder();
            if (decoder == null) {
                return Optional.empty();
            }
            return Optional.of(decoder.decode(token));
        } catch (JwtException ex) {
            log.warn("event=cognito_authentication status=invalid_token message=\"{}\"", ex.getMessage());
            return Optional.empty();
        } catch (Exception ex) {
            log.error("event=cognito_authentication status=decoder_failure message=\"{}\"", ex.getMessage(), ex);
            return Optional.empty();
        }
    }

    private JwtDecoder resolveDecoder() {
        JwtDecoder existing = decoderReference.get();
        if (existing != null) {
            return existing;
        }
        String jwkSetUri = properties.getJwkSetUri();
        String issuer = properties.getIssuerUri();
        if (jwkSetUri == null || issuer == null) {
            log.debug("event=cognito_configuration status=incomplete jwkSetUri={} issuer={}", jwkSetUri, issuer);
            return null;
        }
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefaultWithIssuer(issuer),
                new CognitoAudienceValidator(properties.getAppClientId()));
        decoder.setJwtValidator(validator);
        decoderReference.compareAndSet(null, decoder);
        return decoderReference.get();
    }

    private String extractUsername(Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        if (email != null && !email.isBlank()) {
            return email;
        }
        String cognitoUsername = jwt.getClaimAsString("cognito:username");
        if (cognitoUsername != null && !cognitoUsername.isBlank()) {
            return cognitoUsername;
        }
        return jwt.getSubject();
    }
}
