package com.example.Tucasdesk.service;

import com.example.Tucasdesk.dtos.ChamadoRequest;
import com.example.Tucasdesk.dtos.ChamadoResponseDTO;
import com.example.Tucasdesk.model.*;
import com.example.Tucasdesk.repository.*;
import com.example.Tucasdesk.messaging.ChamadoMessagingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChamadoServiceTest {

    @Mock
    private ChamadoRepository chamadoRepository;
    @Mock
    private CategoriaRepository categoriaRepository;
    @Mock
    private PrioridadeRepository prioridadeRepository;
    @Mock
    private StatusRepository statusRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private InteracaoRepository interacaoRepository;
    @Mock
    private HistoricoStatusRepository historicoStatusRepository;
    @Mock
    private ChamadoMessagingService chamadoMessagingService;

    private ChamadoService chamadoService;

    @BeforeEach
    void setUp() {
        chamadoService = new ChamadoService(
                chamadoRepository,
                categoriaRepository,
                prioridadeRepository,
                statusRepository,
                usuarioRepository,
                interacaoRepository,
                historicoStatusRepository,
                chamadoMessagingService
        );
    }

    @Test
    void deveRegistrarHistoricoAoCriarChamado() {
        ChamadoRequest request = new ChamadoRequest();
        request.setTitulo("Computador não liga");
        request.setDescricao("O equipamento não liga após atualização");
        request.setCategoriaId(2);
        request.setPrioridadeId(3);
        request.setStatusId(1);
        request.setUsuarioId(10);

        Categoria categoria = new Categoria();
        categoria.setIdCategoria(2);
        categoria.setNome("Hardware");

        Prioridade prioridade = new Prioridade();
        prioridade.setIdPrioridade(3);
        prioridade.setNome("Alta");

        Status statusAberto = new Status();
        statusAberto.setIdStatus(1);
        statusAberto.setNome("Aberto");

        Usuario solicitante = new Usuario();
        solicitante.setIdUsuario(10);
        solicitante.setNome("Maria");
        solicitante.setEmail("maria@example.com");

        when(categoriaRepository.findById(2)).thenReturn(Optional.of(categoria));
        when(prioridadeRepository.findById(3)).thenReturn(Optional.of(prioridade));
        when(statusRepository.findById(1)).thenReturn(Optional.of(statusAberto));
        when(usuarioRepository.findById(10)).thenReturn(Optional.of(solicitante));
        when(chamadoRepository.save(any(Chamado.class))).thenAnswer(invocation -> {
            Chamado chamado = invocation.getArgument(0);
            chamado.setIdChamado(50);
            return chamado;
        });
        when(interacaoRepository.findByChamadoOrderByDataInteracaoAsc(any(Chamado.class)))
                .thenReturn(Collections.emptyList());

        HistoricoStatus historicoSalvo = new HistoricoStatus();
        historicoSalvo.setIdHistorico(100);
        historicoSalvo.setStatus(statusAberto);
        historicoSalvo.setDataRegistro(LocalDateTime.now());
        when(historicoStatusRepository.save(any(HistoricoStatus.class))).thenAnswer(invocation -> {
            HistoricoStatus historico = invocation.getArgument(0);
            historico.setIdHistorico(100);
            return historico;
        });
        when(historicoStatusRepository.findByChamadoOrderByDataRegistroAsc(any(Chamado.class)))
                .thenReturn(List.of(historicoSalvo));

        ChamadoResponseDTO resposta = chamadoService.criar(request);

        assertThat(resposta.getId()).isEqualTo(50);
        assertThat(resposta.getDataAbertura()).isNotNull();
        assertThat(resposta.getHistoricoStatus())
                .hasSize(1)
                .first()
                .extracting(dto -> dto.getStatus().getId())
                .isEqualTo(1);

        ArgumentCaptor<HistoricoStatus> historicoCaptor = ArgumentCaptor.forClass(HistoricoStatus.class);
        verify(historicoStatusRepository).save(historicoCaptor.capture());
        HistoricoStatus historicoRegistrado = historicoCaptor.getValue();
        assertThat(historicoRegistrado.getStatus()).isEqualTo(statusAberto);
        assertThat(historicoRegistrado.getDataRegistro()).isNotNull();
        verify(chamadoMessagingService).publishChamadoCreatedEvent(any(Chamado.class));
    }

    @Test
    void deveRegistrarHistoricoAoAtualizarStatus() {
        Chamado chamado = new Chamado();
        chamado.setIdChamado(22);
        Status statusAtual = new Status();
        statusAtual.setIdStatus(1);
        statusAtual.setNome("Aberto");
        chamado.setStatus(statusAtual);

        Status statusFechado = new Status();
        statusFechado.setIdStatus(2);
        statusFechado.setNome("Fechado");

        when(chamadoRepository.findById(22)).thenReturn(Optional.of(chamado));
        when(statusRepository.findById(2)).thenReturn(Optional.of(statusFechado));
        when(chamadoRepository.save(any(Chamado.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(interacaoRepository.findByChamadoOrderByDataInteracaoAsc(chamado))
                .thenReturn(Collections.emptyList());

        HistoricoStatus historicoFechado = new HistoricoStatus();
        historicoFechado.setIdHistorico(200);
        historicoFechado.setStatus(statusFechado);
        historicoFechado.setDataRegistro(LocalDateTime.now());
        when(historicoStatusRepository.save(any(HistoricoStatus.class))).thenAnswer(invocation -> {
            HistoricoStatus historico = invocation.getArgument(0);
            historico.setIdHistorico(200);
            return historico;
        });
        when(historicoStatusRepository.findByChamadoOrderByDataRegistroAsc(chamado))
                .thenReturn(List.of(historicoFechado));

        ChamadoResponseDTO resposta = chamadoService.atualizarStatus(22, 2);

        assertThat(resposta.getStatus().getId()).isEqualTo(2);
        assertThat(resposta.getDataFechamento()).isNotNull();
        assertThat(resposta.getHistoricoStatus()).hasSize(1);

        verify(historicoStatusRepository).save(any(HistoricoStatus.class));
        verify(chamadoMessagingService).publishChamadoStatusChangedEvent(chamado);
    }

    @Test
    void naoDevePermitirAtualizarParaMesmoStatus() {
        Chamado chamado = new Chamado();
        chamado.setIdChamado(35);
        Status statusAtual = new Status();
        statusAtual.setIdStatus(5);
        statusAtual.setNome("Em andamento");
        chamado.setStatus(statusAtual);

        when(chamadoRepository.findById(35)).thenReturn(Optional.of(chamado));
        when(statusRepository.findById(5)).thenReturn(Optional.of(statusAtual));

        assertThatThrownBy(() -> chamadoService.atualizarStatus(35, 5))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("já está com o status informado");

        verify(historicoStatusRepository, never()).save(any());
        verify(chamadoRepository, never()).save(any());
    }
}
