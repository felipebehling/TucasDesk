package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.config.ApiExceptionHandler;
import com.example.Tucasdesk.config.SecurityConfig;
import com.example.Tucasdesk.security.CustomJwtAuthenticationConverter;
import com.example.Tucasdesk.service.ChamadoService;
import com.example.Tucasdesk.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ChamadoController.class, properties = {
    "spring.security.oauth2.resourceserver.jwt.issuer-uri=https://test.issuer.com"
})
@Import({SecurityConfig.class, ApiExceptionHandler.class, CustomJwtAuthenticationConverter.class})
class ChamadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChamadoService chamadoService;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    void deveRemoverInteracaoComSucesso() throws Exception {
        mockMvc.perform(delete("/chamados/{id}/interacoes/{interacaoId}", 5, 8)
                        .param("usuarioId", "10")
                        .with(jwt().authorities(new SimpleGrantedAuthority("TECNICO"))))
                .andExpect(status().isNoContent());

        verify(chamadoService).removerInteracao(5, 8, 10);
    }

    @Test
    void deveRetornarErroQuandoInteracaoNaoPertenceAoChamado() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "A interação não pertence ao chamado informado.")).when(chamadoService)
                .removerInteracao(3, 4, 7);

        mockMvc.perform(delete("/chamados/{id}/interacoes/{interacaoId}", 3, 4)
                        .param("usuarioId", "7")
                        .with(jwt().authorities(new SimpleGrantedAuthority("USUARIO"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("A interação não pertence ao chamado informado."));
    }

    @Test
    void deveAtualizarStatusComSucesso() throws Exception {
        String requestBody = "{\"statusId\": 2}";

        mockMvc.perform(patch("/chamados/{id}/status", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(jwt().authorities(new SimpleGrantedAuthority("TECNICO"))))
                .andExpect(status().isOk());

        verify(chamadoService).atualizarStatus(1, 2);
    }

    @Test
    void deveRetornarErroQuandoStatusNaoInformado() throws Exception {
        String requestBody = "{\"prioridadeId\": 3}"; // Body sem statusId

        mockMvc.perform(patch("/chamados/{id}/status", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(jwt().authorities(new SimpleGrantedAuthority("TECNICO"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O status é obrigatório para atualização parcial."));
    }

    @Test
    void deveAtualizarPrioridadeComSucesso() throws Exception {
        String requestBody = "{\"prioridadeId\": 2}";

        mockMvc.perform(patch("/chamados/{id}/prioridade", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(jwt().authorities(new SimpleGrantedAuthority("TECNICO"))))
                .andExpect(status().isOk());

        verify(chamadoService).atualizarPrioridade(1, 2);
    }

    @Test
    void deveRetornarErroQuandoPrioridadeNaoInformada() throws Exception {
        String requestBody = "{\"statusId\": 3}"; // Body sem prioridadeId

        mockMvc.perform(patch("/chamados/{id}/prioridade", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(jwt().authorities(new SimpleGrantedAuthority("TECNICO"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("A prioridade é obrigatória para atualização parcial."));
    }
}
