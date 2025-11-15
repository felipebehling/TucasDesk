package com.example.Tucasdesk.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.Tucasdesk.config.AwsCognitoProperties;
import com.example.Tucasdesk.model.Perfil;
import com.example.Tucasdesk.model.Usuario;
import com.example.Tucasdesk.repository.PerfilRepository;
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
    private final PerfilRepository perfilRepository;
    private final AtomicReference<JwtDecoder> decoderReference = new AtomicReference<>();

    public CognitoTokenService(AwsCognitoProperties properties, UsuarioRepository usuarioRepository,
            PerfilRepository perfilRepository) {
        this.properties = properties;
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
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

        Optional<Usuario> usuarioOptional = Optional.ofNullable(extractUsername(jwt.get()))
                .flatMap(usuarioRepository::findByEmail);

        Optional<Perfil> perfilOptional = resolvePerfil(jwt.get());
        Collection<? extends GrantedAuthority> authorities;
        if (perfilOptional.isPresent()) {
            authorities = authoritiesForPerfil(perfilOptional.get());
        } else if (usuarioOptional.isPresent()) {
            authorities = usuarioOptional.get().getAuthorities();
        } else {
            authorities = List.of();
        }

        usuarioOptional.ifPresent(usuario -> perfilOptional.ifPresent(usuario::setPerfil));

        if (usuarioOptional.isPresent()) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    usuarioOptional.get(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (!authorities.isEmpty()) {
            Map<String, Object> principal = Map.of(
                    "username", extractUsername(jwt.get()),
                    "claims", jwt.get().getClaims());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null,
                    authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.warn("event=cognito_authentication status=user_not_found subject={}", jwt.get().getSubject());
        }
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

    private Optional<Perfil> resolvePerfil(Jwt jwt) {
        String claimName = properties.getProfileClaim();
        if (!StringUtils.hasText(claimName)) {
            return Optional.empty();
        }

        Object claim = jwt.getClaims().get(claimName);
        if (claim instanceof String) {
            String stringClaim = (String) claim;
            return findPerfil(stringClaim);
        }
        if (claim instanceof Collection<?>) {
            Collection<?> collection = (Collection<?>) claim;
            return collection.stream()
                    .map(Object::toString)
                    .map(this::findPerfil)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst();
        }
        return Optional.empty();
    }

    private Optional<Perfil> findPerfil(String profileName) {
        if (!StringUtils.hasText(profileName)) {
            return Optional.empty();
        }
        return perfilRepository.findByNomeIgnoreCase(profileName.trim());
    }

    private List<GrantedAuthority> authoritiesForPerfil(Perfil perfil) {
        String authority = AuthorityUtils.createRoleAuthority(perfil.getNome());
        return Collections.singletonList(new SimpleGrantedAuthority(authority));
    }
}
