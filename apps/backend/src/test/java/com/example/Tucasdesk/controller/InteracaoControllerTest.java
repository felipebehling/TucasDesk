package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.config.ApiExceptionHandler;
import com.example.Tucasdesk.config.SecurityConfig;
import com.example.Tucasdesk.model.Interacao;
import com.example.Tucasdesk.model.Usuario;
import com.example.Tucasdesk.security.CognitoLogoutHandler;
import com.example.Tucasdesk.service.InteracaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InteracaoController.class)
@Import({SecurityConfig.class, ApiExceptionHandler.class})
public class InteracaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InteracaoService interacaoService;

    @MockBean
    private CognitoLogoutHandler cognitoLogoutHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser // Simulate an authenticated user
    public void createInteraction_shouldReturnCreated() throws Exception {
        Integer ticketId = 1;
        Interacao interacaoToCreate = new Interacao();
        interacaoToCreate.setMensagem("This is a test interaction.");

        Interacao savedInteracao = new Interacao();
        savedInteracao.setIdInteracao(1);
        savedInteracao.setMensagem("This is a test interaction.");
        savedInteracao.setDataInteracao(LocalDateTime.now());
        Usuario author = new Usuario();
        author.setIdUsuario(1);
        author.setNome("Test User");
        author.setAtivo(true);
        savedInteracao.setUsuario(author);

        when(interacaoService.createInteraction(eq(ticketId), any(Interacao.class))).thenReturn(savedInteracao);

        mockMvc.perform(post("/api/chamados/{chamadoId}/interacoes", ticketId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(interacaoToCreate)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.idInteracao").value(1))
                .andExpect(jsonPath("$.mensagem").value("This is a test interaction."));
    }

    @Test
    @WithMockUser
    public void getInteractionsByTicketId_shouldReturnListOfInteractions() throws Exception {
        Integer ticketId = 1;
        Interacao interacao = new Interacao();
        interacao.setIdInteracao(1);
        interacao.setMensagem("Test");

        when(interacaoService.getInteractionsByTicketId(ticketId)).thenReturn(Collections.singletonList(interacao));

        mockMvc.perform(get("/api/chamados/{chamadoId}/interacoes", ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idInteracao").value(1))
                .andExpect(jsonPath("$[0].mensagem").value("Test"));
    }
}
