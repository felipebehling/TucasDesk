package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.config.ApiExceptionHandler;
import com.example.Tucasdesk.config.SecurityConfig;
import com.example.Tucasdesk.model.Usuario;
import com.example.Tucasdesk.repository.UsuarioRepository;
import com.example.Tucasdesk.security.CustomJwtAuthenticationConverter;
import com.example.Tucasdesk.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsuarioController.class, properties = {
    "spring.security.oauth2.resourceserver.jwt.issuer-uri=https://test.issuer.com"
})
@Import({SecurityConfig.class, ApiExceptionHandler.class, CustomJwtAuthenticationConverter.class})
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private UsuarioService usuarioService; // Necessário para o CustomJwtAuthenticationConverter

    @Test
    void deveRetornarDadosDoUsuarioAutenticado() throws Exception {
        String cognitoId = "test-cognito-id-123";

        Usuario usuarioMock = new Usuario();
        usuarioMock.setIdUsuario(1);
        usuarioMock.setCognitoId(cognitoId);
        usuarioMock.setNome("Test User");
        usuarioMock.setEmail("test@example.com");
        usuarioMock.setAtivo(true);
        usuarioMock.setDataCriacao(LocalDateTime.now());

        when(usuarioRepository.findByCognitoId(cognitoId)).thenReturn(Optional.of(usuarioMock));

        mockMvc.perform(get("/api/usuarios/me")
                        .with(jwt().authorities(new SimpleGrantedAuthority("USUARIO"))
                                .jwt(jwt -> jwt
                                        .subject(cognitoId)
                                        .claim("scope", "openid")
                                )
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.nome").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void deveRetornarNotFoundQuandoUsuarioNaoExisteNoBanco() throws Exception {
        String cognitoId = "non-existent-user";

        when(usuarioRepository.findByCognitoId(cognitoId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/me")
                        .with(jwt().authorities(new SimpleGrantedAuthority("USUARIO"))
                                .jwt(jwt -> jwt
                                        .subject(cognitoId)
                                        .claim("scope", "openid")
                                )
                        ))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveListarUsuariosParaAdmin() throws Exception {
        when(usuarioService.listarUsuarios()).thenReturn(List.of());

        mockMvc.perform(get("/api/usuarios")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMINISTRADOR"))))
                .andExpect(status().isOk());
    }

    @Test
    void naoDeveListarUsuariosParaNaoAdmin() throws Exception {
        mockMvc.perform(get("/api/usuarios")
                        .with(jwt().authorities(new SimpleGrantedAuthority("USUARIO"))))
                .andExpect(status().isForbidden());
    }
}
