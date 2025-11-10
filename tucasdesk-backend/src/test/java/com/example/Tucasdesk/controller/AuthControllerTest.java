package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.config.SecurityConfig;
import com.example.Tucasdesk.dtos.RegisterRequest;
import com.example.Tucasdesk.dtos.UsuarioResponseDTO;
import com.example.Tucasdesk.repository.UsuarioRepository;
import com.example.Tucasdesk.security.CognitoAuthenticationFilter;
import com.example.Tucasdesk.security.CognitoService;
import com.example.Tucasdesk.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.doAnswer;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private CognitoService cognitoService;

    @MockBean
    private CognitoAuthenticationFilter cognitoAuthenticationFilter;

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
    @DisplayName("Deve registrar um usuário com sucesso")
    void deveRegistrarUsuarioComSucesso() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setNome("Usuário Teste");
        request.setEmail("teste@example.com");
        request.setSenha("SenhaForte#1");
        request.setConfirmacaoSenha("SenhaForte#1");

        when(usuarioRepository.findByEmail("teste@example.com")).thenReturn(Optional.empty());
        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO(
                1,
                "Usuário Teste",
                "teste@example.com",
                null,
                LocalDateTime.now(),
                true
        );
        when(usuarioService.criarUsuario(any(RegisterRequest.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("teste@example.com"))
                .andExpect(jsonPath("$.senha").doesNotExist());
    }

    @Test
    @DisplayName("Não deve registrar quando o e-mail já existe")
    void naoDeveRegistrarQuandoEmailDuplicado() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setNome("Usuário Teste");
        request.setEmail("duplicado@example.com");
        request.setSenha("SenhaForte#1");
        request.setConfirmacaoSenha("SenhaForte#1");

        when(usuarioRepository.findByEmail("duplicado@example.com")).thenReturn(Optional.of(new com.example.Tucasdesk.model.Usuario()));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Já existe um usuário cadastrado com este e-mail."));
    }

    @Test
    @DisplayName("Não deve registrar quando a senha é fraca")
    void naoDeveRegistrarQuandoSenhaFraca() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setNome("Usuário Teste");
        request.setEmail("fraca@example.com");
        request.setSenha("senha123");
        request.setConfirmacaoSenha("senha123");

        when(usuarioRepository.findByEmail("fraca@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("A senha deve conter letras maiúsculas, minúsculas, números e caracteres especiais."));
    }

    @Test
    @DisplayName("Não deve registrar quando as senhas não conferem")
    void naoDeveRegistrarQuandoConfirmacaoInvalida() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setNome("Usuário Teste");
        request.setEmail("confirmacao@example.com");
        request.setSenha("SenhaForte#1");
        request.setConfirmacaoSenha("SenhaForte#2");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("A confirmação da senha deve coincidir com a senha informada."));
    }
}
