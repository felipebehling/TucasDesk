package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.config.ApiExceptionHandler;
import com.example.Tucasdesk.service.ChamadoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.Tucasdesk.config.SecurityConfig;
import com.example.Tucasdesk.security.CognitoLogoutHandler;

@WebMvcTest(ChamadoController.class)
@Import({SecurityConfig.class, ApiExceptionHandler.class})
class ChamadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChamadoService chamadoService;

    @MockBean
    private CognitoLogoutHandler cognitoLogoutHandler;


    @Test
    @WithMockUser(roles = "TECNICO")
    void deveRemoverInteracaoComSucesso() throws Exception {
        mockMvc.perform(delete("/chamados/{id}/interacoes/{interacaoId}", 5, 8)
                        .param("usuarioId", "10"))
                .andExpect(status().isNoContent());

        verify(chamadoService).removerInteracao(5, 8, 10);
    }

    @Test
    @WithMockUser(roles = "USUARIO")
    void deveRetornarErroQuandoInteracaoNaoPertenceAoChamado() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "A interação não pertence ao chamado informado.")).when(chamadoService)
                .removerInteracao(3, 4, 7);

        mockMvc.perform(delete("/chamados/{id}/interacoes/{interacaoId}", 3, 4)
                        .param("usuarioId", "7"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("A interação não pertence ao chamado informado."));
    }

    @Test
    @WithMockUser(roles = "TECNICO")
    void deveAtualizarStatusComSucesso() throws Exception {
        String requestBody = "{\"statusId\": 2}";

        mockMvc.perform(patch("/chamados/{id}/status", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(chamadoService).atualizarStatus(1, 2);
    }

    @Test
    @WithMockUser(roles = "TECNICO")
    void deveRetornarErroQuandoStatusNaoInformado() throws Exception {
        String requestBody = "{\"prioridadeId\": 3}"; // Body sem statusId

        mockMvc.perform(patch("/chamados/{id}/status", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O status é obrigatório para atualização parcial."));
    }

    @Test
    @WithMockUser(roles = "TECNICO")
    void deveAtualizarPrioridadeComSucesso() throws Exception {
        String requestBody = "{\"prioridadeId\": 2}";

        mockMvc.perform(patch("/chamados/{id}/prioridade", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(chamadoService).atualizarPrioridade(1, 2);
    }

    @Test
    @WithMockUser(roles = "TECNICO")
    void deveRetornarErroQuandoPrioridadeNaoInformada() throws Exception {
        String requestBody = "{\"statusId\": 3}"; // Body sem prioridadeId

        mockMvc.perform(patch("/chamados/{id}/prioridade", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("A prioridade é obrigatória para atualização parcial."));
    }
}
