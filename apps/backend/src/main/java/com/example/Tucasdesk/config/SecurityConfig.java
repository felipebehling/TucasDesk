package com.example.Tucasdesk.config;

import com.example.Tucasdesk.security.CustomJwtAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomJwtAuthenticationConverter customJwtAuthenticationConverter;

    public SecurityConfig(CustomJwtAuthenticationConverter customJwtAuthenticationConverter) {
        this.customJwtAuthenticationConverter = customJwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categorias/**", "/api/prioridades/**", "/api/status/**",
                                "/api/perfis/**").hasAnyAuthority("ADMINISTRADOR", "TECNICO", "USUARIO")
                        .requestMatchers(HttpMethod.POST, "/api/categorias/**", "/api/prioridades/**", "/api/status/**",
                                "/api/perfis/**").hasAuthority("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/categorias/**", "/api/prioridades/**", "/api/status/**",
                                "/api/perfis/**").hasAuthority("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/categorias/**", "/api/prioridades/**", "/api/status/**",
                                "/api/perfis/**").hasAuthority("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/me").hasAnyAuthority("ADMINISTRADOR", "TECNICO",
                                "USUARIO")
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/me").hasAnyAuthority("ADMINISTRADOR", "TECNICO",
                                "USUARIO")
                        .requestMatchers("/api/usuarios/**").hasAuthority("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/chamados/**").hasAnyAuthority("ADMINISTRADOR", "TECNICO",
                                "USUARIO")
                        .requestMatchers(HttpMethod.POST, "/chamados/**").hasAnyAuthority("ADMINISTRADOR", "TECNICO",
                                "USUARIO")
                        .requestMatchers(HttpMethod.PUT, "/chamados/**").hasAnyAuthority("ADMINISTRADOR", "TECNICO")
                        .requestMatchers(HttpMethod.PATCH, "/chamados/**").hasAnyAuthority("ADMINISTRADOR", "TECNICO")
                        .requestMatchers(HttpMethod.DELETE, "/chamados/**").hasAnyAuthority("ADMINISTRADOR", "TECNICO", "USUARIO")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(customJwtAuthenticationConverter)
                        )
                );
        return http.build();
    }
}
