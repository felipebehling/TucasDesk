package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.dtos.ChamadoRequestDTO;
import com.example.Tucasdesk.model.Chamado;
import com.example.Tucasdesk.model.Status;
import com.example.Tucasdesk.repository.ChamadoRepository;
import com.example.Tucasdesk.repository.StatusRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ChamadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChamadoRepository chamadoRepository;

    @MockBean
    private StatusRepository statusRepository;

    @Test
    public void whenCreateChamado_thenSetDataAberturaAndStatus() throws Exception {
        // Given
        ChamadoRequestDTO requestDTO = new ChamadoRequestDTO();
        requestDTO.setTitulo("Test Title");
        requestDTO.setDescricao("Test Description");

        Status statusAberto = new Status();
        statusAberto.setIdStatus(1);
        statusAberto.setNome("Aberto");

        Chamado savedChamado = new Chamado();
        savedChamado.setIdChamado(1);
        savedChamado.setTitulo(requestDTO.getTitulo());
        savedChamado.setDescricao(requestDTO.getDescricao());
        savedChamado.setDataAbertura(LocalDateTime.now());
        savedChamado.setStatus(statusAberto);

        when(statusRepository.findByNome("Aberto")).thenReturn(Optional.of(statusAberto));
        when(chamadoRepository.save(any(Chamado.class))).thenReturn(savedChamado);

        // When & Then
        mockMvc.perform(post("/api/chamados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idChamado").value(1))
                .andExpect(jsonPath("$.titulo").value("Test Title"))
                .andExpect(jsonPath("$.dataAbertura").exists())
                .andExpect(jsonPath("$.status.nome").value("Aberto"));
    }
}
