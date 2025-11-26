package com.example.Tucasdesk.security;

import com.example.Tucasdesk.service.UsuarioService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UsuarioService usuarioService;

    public CustomJwtAuthenticationConverter(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // JIT Provisioning
        usuarioService.findOrCreateFromJwt(jwt);

        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, authorities);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        List<String> groups = jwt.getClaimAsStringList("cognito:groups");
        if (groups == null) {
            return List.of();
        }
        return groups.stream()
                .map(group -> new SimpleGrantedAuthority(group.toUpperCase()))
                .collect(Collectors.toList());
    }
}
