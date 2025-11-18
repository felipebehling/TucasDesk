package com.example.Tucasdesk.config;

import com.example.Tucasdesk.security.CognitoAuthenticationFilter;
import com.example.Tucasdesk.security.CognitoLogoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the application.
 * This class configures web security, including CSRF protection and request authorization.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CognitoAuthenticationFilter cognitoAuthenticationFilter;
    private final CognitoLogoutHandler cognitoLogoutHandler;

    /**
     * Constructs a new {@code SecurityConfig} with the specified authentication filter and logout handler.
     *
     * @param cognitoAuthenticationFilter The filter responsible for authenticating requests using Cognito tokens.
     * @param cognitoLogoutHandler        The handler responsible for processing logout requests with Cognito.
     */
    public SecurityConfig(CognitoAuthenticationFilter cognitoAuthenticationFilter,
            CognitoLogoutHandler cognitoLogoutHandler) {
        this.cognitoAuthenticationFilter = cognitoAuthenticationFilter;
        this.cognitoLogoutHandler = cognitoLogoutHandler;
    }

    /**
     * Configures the security filter chain.
     * This method disables CSRF and permits all requests.
     *
     * @param http The {@link HttpSecurity} to configure.
     * @return The configured {@link SecurityFilterChain}.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(logout -> logout.logoutSuccessHandler(cognitoLogoutHandler))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
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
                .addFilterBefore(cognitoAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Provides a password encoder bean.
     * Uses BCrypt for strong, salted password hashing.
     *
     * @return A {@link PasswordEncoder} instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
