package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.config.ApiExceptionHandler;
import com.example.Tucasdesk.dtos.UsuarioResponseDTO;
import com.example.Tucasdesk.model.Usuario;
import com.example.Tucasdesk.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.Tucasdesk.config.SecurityConfig;
import com.example.Tucasdesk.security.CognitoAuthenticationFilter;
import com.example.Tucasdesk.security.CognitoLogoutHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.doAnswer;

@WebMvcTest(UsuarioController.class)
@Import({SecurityConfig.class, ApiExceptionHandler.class})
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private CognitoLogoutHandler cognitoLogoutHandler;

    @MockBean
    private CognitoAuthenticationFilter cognitoAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setupFilterChain() throws Exception {
        doAnswer(invocation -> {
            HttpServletRequest request = invocation.getArgument(0);
            HttpServletResponse response = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(request, response);
            return null;
        }).when(cognitoAuthenticationFilter).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterChain.class));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void testCriarUsuario() throws Exception {
        String payload = "{" +
                "\"nome\":\"Test User\"," +
                "\"email\":\"test@example.com\"," +
                "\"senha\":\"password\"" +
                "}";

        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO(1, "Test User", "test@example.com", null,
                LocalDateTime.now(), true);

        when(usuarioService.criarUsuario(any(Usuario.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.senha").doesNotExist());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void testCriarUsuario_comNomeInvalido() throws Exception {
        String payload = "{" +
                "\"nome\":\"\"," +
                "\"email\":\"test@example.com\"," +
                "\"senha\":\"password\"" +
                "}";

        mockMvc.perform(post("/api/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O nome não pode estar em branco"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void testListarTodosSemSenha() throws Exception {
        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO(1, "Test User", "test@example.com", null,
                LocalDateTime.now(), true);

        when(usuarioService.listarUsuarios()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/usuarios").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idUsuario").value(1))
                .andExpect(jsonPath("$[0].nome").value("Test User"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].senha").doesNotExist());
    }
}
