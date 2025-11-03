package com.example.Tucasdesk.service;

import com.example.Tucasdesk.dtos.*;
import com.example.Tucasdesk.model.*;
import com.example.Tucasdesk.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @InjectMocks
    private ChamadoService chamadoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarChamadoComRelacionamentos() {
        ChamadoRequest request = new ChamadoRequest();
        request.setTitulo("Problema no equipamento");
        request.setDescricao("Equipamento não liga");
        request.setCategoriaId(10);
        request.setPrioridadeId(20);
        request.setStatusId(30);
        request.setUsuarioId(1);
        request.setTecnicoId(2);

        Categoria categoria = new Categoria();
        categoria.setIdCategoria(10);
        categoria.setNome("Hardware");
        when(categoriaRepository.findById(10)).thenReturn(Optional.of(categoria));

        Prioridade prioridade = new Prioridade();
        prioridade.setIdPrioridade(20);
        prioridade.setNome("Alta");
        when(prioridadeRepository.findById(20)).thenReturn(Optional.of(prioridade));

        Status status = new Status();
        status.setIdStatus(30);
        status.setNome("Aberto");
        when(statusRepository.findById(30)).thenReturn(Optional.of(status));

        Usuario solicitante = new Usuario();
        solicitante.setIdUsuario(1);
        solicitante.setNome("Maria");
        solicitante.setEmail("maria@example.com");
        Usuario tecnico = new Usuario();
        tecnico.setIdUsuario(2);
        tecnico.setNome("João");
        tecnico.setEmail("joao@example.com");
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(solicitante));
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(tecnico));

        when(chamadoRepository.save(any(Chamado.class))).thenAnswer(invocation -> {
            Chamado chamado = invocation.getArgument(0);
            chamado.setIdChamado(99);
            return chamado;
        });
        when(interacaoRepository.findByChamadoOrderByDataInteracaoAsc(any(Chamado.class)))
                .thenReturn(Collections.emptyList());

        ChamadoResponseDTO response = chamadoService.criar(request);

        assertNotNull(response);
        assertEquals(99, response.getId());
        assertEquals("Problema no equipamento", response.getTitulo());
        assertEquals("Hardware", response.getCategoria().getNome());
        assertEquals("Alta", response.getPrioridade().getNome());
        assertEquals("Aberto", response.getStatus().getNome());
        assertEquals("Maria", response.getSolicitante().getNome());
        assertEquals("João", response.getTecnico().getNome());
        assertNotNull(response.getDataAbertura());
    }

    @Test
    void deveAtualizarStatusEAjustarDataFechamento() {
        Status statusAtual = new Status();
        statusAtual.setIdStatus(1);
        statusAtual.setNome("Em andamento");

        Status statusFechado = new Status();
        statusFechado.setIdStatus(2);
        statusFechado.setNome("Fechado");

        Chamado chamado = new Chamado();
        chamado.setIdChamado(50);
        chamado.setStatus(statusAtual);

        when(chamadoRepository.findById(50)).thenReturn(Optional.of(chamado));
        when(statusRepository.findById(2)).thenReturn(Optional.of(statusFechado));
        when(chamadoRepository.save(any(Chamado.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(interacaoRepository.findByChamadoOrderByDataInteracaoAsc(any(Chamado.class)))
                .thenReturn(Collections.emptyList());

        ChamadoResponseDTO response = chamadoService.atualizarStatus(50, 2);

        assertNotNull(response.getDataFechamento());
        assertEquals("Fechado", response.getStatus().getNome());
    }

    @Test
    void deveAdicionarInteracao() {
        Chamado chamado = new Chamado();
        chamado.setIdChamado(7);
        when(chamadoRepository.findById(7)).thenReturn(Optional.of(chamado));

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(3);
        usuario.setNome("Analista");
        usuario.setEmail("analista@example.com");
        when(usuarioRepository.findById(3)).thenReturn(Optional.of(usuario));

        when(interacaoRepository.save(any(Interacao.class))).thenAnswer(invocation -> {
            Interacao interacao = invocation.getArgument(0);
            interacao.setIdInteracao(123);
            return interacao;
        });

        InteracaoRequest request = new InteracaoRequest();
        request.setUsuarioId(3);
        request.setMensagem("Atualização do chamado");
        request.setAnexoUrl("http://anexo");

        InteracaoResponseDTO response = chamadoService.adicionarInteracao(7, request);

        assertEquals(123, response.getId());
        assertEquals("Atualização do chamado", response.getMensagem());
        assertEquals("http://anexo", response.getAnexoUrl());
        assertEquals("Analista", response.getUsuario().getNome());

        ArgumentCaptor<Interacao> captor = ArgumentCaptor.forClass(Interacao.class);
        verify(interacaoRepository).save(captor.capture());
        assertEquals("Atualização do chamado", captor.getValue().getMensagem());
        assertNotNull(captor.getValue().getDataInteracao());
    }

    @Test
    void deveFalharAoRepetirMesmoStatus() {
        Status status = new Status();
        status.setIdStatus(5);
        status.setNome("Aberto");

        Chamado chamado = new Chamado();
        chamado.setIdChamado(80);
        chamado.setStatus(status);

        when(chamadoRepository.findById(80)).thenReturn(Optional.of(chamado));
        when(statusRepository.findById(5)).thenReturn(Optional.of(status));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> chamadoService.atualizarStatus(80, 5));

        assertEquals("O chamado já está com o status informado.", exception.getReason());
    }
}
