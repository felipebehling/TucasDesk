package com.example.Tucasdesk.config;

import com.example.Tucasdesk.security.CognitoLogoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CognitoLogoutHandler cognitoLogoutHandler;

    public SecurityConfig(CognitoLogoutHandler cognitoLogoutHandler) {
        this.cognitoLogoutHandler = cognitoLogoutHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(logout -> logout.logoutSuccessHandler(cognitoLogoutHandler))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categorias/**", "/api/prioridades/**", "/api/status/**",
                                "/api/perfis/**").hasAnyRole("ADMINISTRADOR", "TECNICO", "USUARIO")
                        .requestMatchers(HttpMethod.POST, "/api/categorias/**", "/api/prioridades/**", "/api/status/**",
                                "/api/perfis/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/categorias/**", "/api/prioridades/**", "/api/status/**",
                                "/api/perfis/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/categorias/**", "/api/prioridades/**", "/api/status/**",
                                "/api/perfis/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/me").hasAnyRole("ADMINISTRADOR", "TECNICO",
                                "USUARIO")
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/me").hasAnyRole("ADMINISTRADOR", "TECNICO",
                                "USUARIO")
                        .requestMatchers("/api/usuarios/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/chamados/**").hasAnyRole("ADMINISTRADOR", "TECNICO",
                                "USUARIO")
                        .requestMatchers(HttpMethod.POST, "/chamados/**").hasAnyRole("ADMINISTRADOR", "TECNICO",
                                "USUARIO")
                        .requestMatchers(HttpMethod.PUT, "/chamados/**").hasAnyRole("ADMINISTRADOR", "TECNICO")
                        .requestMatchers(HttpMethod.PATCH, "/chamados/**").hasAnyRole("ADMINISTRADOR", "TECNICO")
                        .requestMatchers(HttpMethod.DELETE, "/chamados/**").hasAnyRole("ADMINISTRADOR", "TECNICO", "USUARIO")
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("http://localhost:3000", true));
        return http.build();
    }
}
