package com.example.Tucasdesk.service;

import com.example.Tucasdesk.dtos.*;
import com.example.Tucasdesk.mappers.ChamadoMapper;
import com.example.Tucasdesk.model.*;
import com.example.Tucasdesk.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service responsible for business operations related to {@link Chamado}.
 */
@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final CategoriaRepository categoriaRepository;
    private final PrioridadeRepository prioridadeRepository;
    private final StatusRepository statusRepository;
    private final UsuarioRepository usuarioRepository;
    private final InteracaoRepository interacaoRepository;

    public ChamadoService(ChamadoRepository chamadoRepository,
                          CategoriaRepository categoriaRepository,
                          PrioridadeRepository prioridadeRepository,
                          StatusRepository statusRepository,
                          UsuarioRepository usuarioRepository,
                          InteracaoRepository interacaoRepository) {
        this.chamadoRepository = chamadoRepository;
        this.categoriaRepository = categoriaRepository;
        this.prioridadeRepository = prioridadeRepository;
        this.statusRepository = statusRepository;
        this.usuarioRepository = usuarioRepository;
        this.interacaoRepository = interacaoRepository;
    }

    /**
     * Lists all tickets including their interactions timeline.
     *
     * @return a list of {@link ChamadoResponseDTO}.
     */
    public List<ChamadoResponseDTO> listarTodos() {
        return chamadoRepository.findAll().stream()
                .map(chamado -> ChamadoMapper.toChamadoResponseDTO(chamado, carregarInteracoes(chamado)))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single ticket by its identifier.
     *
     * @param id the ticket identifier.
     * @return the mapped response DTO.
     */
    public ChamadoResponseDTO buscarPorId(Integer id) {
        Chamado chamado = obterChamado(id);
        return ChamadoMapper.toChamadoResponseDTO(chamado, carregarInteracoes(chamado));
    }

    /**
     * Creates a new ticket.
     *
     * @param request the data used to create the ticket.
     * @return the created ticket mapped to a DTO.
     */
    public ChamadoResponseDTO criar(ChamadoRequest request) {
        request.validate();
        Chamado chamado = new Chamado();
        preencherDadosBasicos(chamado, request.getTitulo(), request.getDescricao());
        aplicarRelacoes(chamado, request.getCategoriaId(), request.getPrioridadeId(), request.getStatusId());
        chamado.setUsuario(buscarUsuario(request.getUsuarioId()));
        if (request.getTecnicoId() != null) {
            chamado.setTecnico(buscarUsuario(request.getTecnicoId()));
        }
        chamado.setDataAbertura(LocalDateTime.now());
        ajustarDataFechamento(chamado);
        Chamado salvo = chamadoRepository.save(chamado);
        return ChamadoMapper.toChamadoResponseDTO(salvo, carregarInteracoes(salvo));
    }

    /**
     * Updates a ticket replacing mutable fields.
     *
     * @param id      the ticket identifier.
     * @param request the payload with the desired changes.
     * @return the updated ticket mapped to a DTO.
     */
    public ChamadoResponseDTO atualizar(Integer id, ChamadoUpdateRequest request) {
        Chamado chamado = obterChamado(id);
        if (request.getTitulo() != null) {
            chamado.setTitulo(request.getTitulo());
        }
        if (request.getDescricao() != null) {
            chamado.setDescricao(request.getDescricao());
        }
        if (request.getCategoriaId() != null) {
            chamado.setCategoria(buscarCategoria(request.getCategoriaId()));
        }
        if (request.getPrioridadeId() != null) {
            chamado.setPrioridade(buscarPrioridade(request.getPrioridadeId()));
        }
        if (request.getStatusId() != null) {
            chamado.setStatus(buscarStatus(request.getStatusId()));
        }
        if (request.getTecnicoId() != null) {
            chamado.setTecnico(buscarUsuario(request.getTecnicoId()));
        }
        ajustarDataFechamento(chamado);
        Chamado salvo = chamadoRepository.save(chamado);
        return ChamadoMapper.toChamadoResponseDTO(salvo, carregarInteracoes(salvo));
    }

    /**
     * Updates the status of the ticket.
     *
     * @param id       the ticket identifier.
     * @param statusId the new status identifier.
     * @return the updated ticket mapped to a DTO.
     */
    public ChamadoResponseDTO atualizarStatus(Integer id, Integer statusId) {
        Chamado chamado = obterChamado(id);
        Status novoStatus = buscarStatus(statusId);
        validarTransicaoStatus(chamado.getStatus(), novoStatus);
        chamado.setStatus(novoStatus);
        ajustarDataFechamento(chamado);
        Chamado salvo = chamadoRepository.save(chamado);
        return ChamadoMapper.toChamadoResponseDTO(salvo, carregarInteracoes(salvo));
    }

    /**
     * Adds a new interaction to a ticket.
     *
     * @param chamadoId the ticket identifier.
     * @param request   the interaction payload.
     * @return the created interaction mapped to DTO.
     */
    public InteracaoResponseDTO adicionarInteracao(Integer chamadoId, InteracaoRequest request) {
        if (request.getUsuarioId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuário da interação é obrigatório.");
        }
        if (request.getMensagem() == null || request.getMensagem().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A mensagem da interação é obrigatória.");
        }
        Chamado chamado = obterChamado(chamadoId);
        Usuario usuario = buscarUsuario(request.getUsuarioId());

        Interacao interacao = new Interacao();
        interacao.setChamado(chamado);
        interacao.setUsuario(usuario);
        interacao.setMensagem(request.getMensagem());
        interacao.setAnexoUrl(request.getAnexoUrl());
        interacao.setDataInteracao(LocalDateTime.now());

        Interacao salvo = interacaoRepository.save(interacao);
        return ChamadoMapper.toInteracaoResponseDTO(salvo);
    }

    private List<Interacao> carregarInteracoes(Chamado chamado) {
        return interacaoRepository.findByChamadoOrderByDataInteracaoAsc(chamado);
    }

    private Chamado obterChamado(Integer id) {
        Optional<Chamado> chamadoOpt = chamadoRepository.findById(id);
        return chamadoOpt.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chamado não encontrado."));
    }

    private void preencherDadosBasicos(Chamado chamado, String titulo, String descricao) {
        chamado.setTitulo(titulo);
        chamado.setDescricao(descricao);
    }

    private void aplicarRelacoes(Chamado chamado, Integer categoriaId, Integer prioridadeId, Integer statusId) {
        if (categoriaId != null) {
            chamado.setCategoria(buscarCategoria(categoriaId));
        }
        if (prioridadeId != null) {
            chamado.setPrioridade(buscarPrioridade(prioridadeId));
        }
        chamado.setStatus(buscarStatus(statusId));
    }

    private Categoria buscarCategoria(Integer categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não encontrada."));
    }

    private Prioridade buscarPrioridade(Integer prioridadeId) {
        return prioridadeRepository.findById(prioridadeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prioridade não encontrada."));
    }

    private Status buscarStatus(Integer statusId) {
        return statusRepository.findById(statusId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status não encontrado."));
    }

    private Usuario buscarUsuario(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado."));
    }

    private void ajustarDataFechamento(Chamado chamado) {
        Status status = chamado.getStatus();
        if (status != null && status.getNome() != null && status.getNome().equalsIgnoreCase("Fechado")) {
            if (chamado.getDataFechamento() == null) {
                chamado.setDataFechamento(LocalDateTime.now());
            }
        } else {
            chamado.setDataFechamento(null);
        }
    }

    private void validarTransicaoStatus(Status statusAtual, Status novoStatus) {
        if (statusAtual != null && statusAtual.equals(novoStatus)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O chamado já está com o status informado.");
        }
    }
}
