package com.example.Tucasdesk.service;

import com.example.Tucasdesk.dtos.LookupRequestDTO;
import com.example.Tucasdesk.dtos.LookupResponseDTO;
import com.example.Tucasdesk.model.Categoria;
import com.example.Tucasdesk.model.Perfil;
import com.example.Tucasdesk.model.Prioridade;
import com.example.Tucasdesk.model.Status;
import com.example.Tucasdesk.repository.CategoriaRepository;
import com.example.Tucasdesk.repository.PerfilRepository;
import com.example.Tucasdesk.repository.PrioridadeRepository;
import com.example.Tucasdesk.repository.StatusRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsible for CRUD operations of lightweight lookup entities used by the ticket flow.
 */
@Service
public class LookupService {

    private final CategoriaRepository categoriaRepository;
    private final PrioridadeRepository prioridadeRepository;
    private final StatusRepository statusRepository;
    private final PerfilRepository perfilRepository;

    public LookupService(CategoriaRepository categoriaRepository,
                         PrioridadeRepository prioridadeRepository,
                         StatusRepository statusRepository,
                         PerfilRepository perfilRepository) {
        this.categoriaRepository = categoriaRepository;
        this.prioridadeRepository = prioridadeRepository;
        this.statusRepository = statusRepository;
        this.perfilRepository = perfilRepository;
    }

    public List<LookupResponseDTO> listarCategorias() {
        return categoriaRepository.findAll().stream()
                .map(categoria -> new LookupResponseDTO(categoria.getIdCategoria(), categoria.getNome()))
                .collect(Collectors.toList());
    }

    public LookupResponseDTO buscarCategoria(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada."));
        return new LookupResponseDTO(categoria.getIdCategoria(), categoria.getNome());
    }

    public LookupResponseDTO criarCategoria(LookupRequestDTO request) {
        Categoria categoria = new Categoria();
        categoria.setNome(validarNome(request.getNome()));
        Categoria salvo = categoriaRepository.save(categoria);
        return new LookupResponseDTO(salvo.getIdCategoria(), salvo.getNome());
    }

    public LookupResponseDTO atualizarCategoria(Integer id, LookupRequestDTO request) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada."));
        categoria.setNome(validarNome(request.getNome()));
        Categoria salvo = categoriaRepository.save(categoria);
        return new LookupResponseDTO(salvo.getIdCategoria(), salvo.getNome());
    }

    public void removerCategoria(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada."));
        removerEntidade(() -> categoriaRepository.delete(categoria), "Não foi possível remover a categoria pois ela está em uso.");
    }

    public List<LookupResponseDTO> listarPrioridades() {
        return prioridadeRepository.findAll().stream()
                .map(prioridade -> new LookupResponseDTO(prioridade.getIdPrioridade(), prioridade.getNome()))
                .collect(Collectors.toList());
    }

    public LookupResponseDTO buscarPrioridade(Integer id) {
        Prioridade prioridade = prioridadeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prioridade não encontrada."));
        return new LookupResponseDTO(prioridade.getIdPrioridade(), prioridade.getNome());
    }

    public LookupResponseDTO criarPrioridade(LookupRequestDTO request) {
        Prioridade prioridade = new Prioridade();
        prioridade.setNome(validarNome(request.getNome()));
        Prioridade salvo = prioridadeRepository.save(prioridade);
        return new LookupResponseDTO(salvo.getIdPrioridade(), salvo.getNome());
    }

    public LookupResponseDTO atualizarPrioridade(Integer id, LookupRequestDTO request) {
        Prioridade prioridade = prioridadeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prioridade não encontrada."));
        prioridade.setNome(validarNome(request.getNome()));
        Prioridade salvo = prioridadeRepository.save(prioridade);
        return new LookupResponseDTO(salvo.getIdPrioridade(), salvo.getNome());
    }

    public void removerPrioridade(Integer id) {
        Prioridade prioridade = prioridadeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prioridade não encontrada."));
        removerEntidade(() -> prioridadeRepository.delete(prioridade), "Não foi possível remover a prioridade pois ela está em uso.");
    }

    public List<LookupResponseDTO> listarStatus() {
        return statusRepository.findAll().stream()
                .map(status -> new LookupResponseDTO(status.getIdStatus(), status.getNome()))
                .collect(Collectors.toList());
    }

    public LookupResponseDTO buscarStatus(Integer id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status não encontrado."));
        return new LookupResponseDTO(status.getIdStatus(), status.getNome());
    }

    public LookupResponseDTO criarStatus(LookupRequestDTO request) {
        Status status = new Status();
        status.setNome(validarNome(request.getNome()));
        Status salvo = statusRepository.save(status);
        return new LookupResponseDTO(salvo.getIdStatus(), salvo.getNome());
    }

    public LookupResponseDTO atualizarStatus(Integer id, LookupRequestDTO request) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status não encontrado."));
        status.setNome(validarNome(request.getNome()));
        Status salvo = statusRepository.save(status);
        return new LookupResponseDTO(salvo.getIdStatus(), salvo.getNome());
    }

    public void removerStatus(Integer id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status não encontrado."));
        removerEntidade(() -> statusRepository.delete(status), "Não foi possível remover o status pois ele está em uso.");
    }

    public List<LookupResponseDTO> listarPerfis() {
        return perfilRepository.findAll().stream()
                .map(perfil -> new LookupResponseDTO(perfil.getIdPerfil(), perfil.getNome()))
                .collect(Collectors.toList());
    }

    public LookupResponseDTO buscarPerfil(Integer id) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil não encontrado."));
        return new LookupResponseDTO(perfil.getIdPerfil(), perfil.getNome());
    }

    public LookupResponseDTO criarPerfil(LookupRequestDTO request) {
        Perfil perfil = new Perfil();
        perfil.setNome(validarNome(request.getNome()));
        Perfil salvo = perfilRepository.save(perfil);
        return new LookupResponseDTO(salvo.getIdPerfil(), salvo.getNome());
    }

    public LookupResponseDTO atualizarPerfil(Integer id, LookupRequestDTO request) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil não encontrado."));
        perfil.setNome(validarNome(request.getNome()));
        Perfil salvo = perfilRepository.save(perfil);
        return new LookupResponseDTO(salvo.getIdPerfil(), salvo.getNome());
    }

    public void removerPerfil(Integer id) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil não encontrado."));
        removerEntidade(() -> perfilRepository.delete(perfil), "Não foi possível remover o perfil pois ele está em uso.");
    }

    private String validarNome(String nome) {
        if (!StringUtils.hasText(nome)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um nome válido.");
        }
        return nome.trim();
    }

    private void removerEntidade(Runnable action, String mensagemConflito) {
        try {
            action.run();
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, mensagemConflito);
        }
    }
}
