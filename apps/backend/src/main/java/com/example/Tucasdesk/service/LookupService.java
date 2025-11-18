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

    /**
     * Lists all categories.
     *
     * @return A list of all categories as {@link LookupResponseDTO}.
     */
    public List<LookupResponseDTO> listarCategorias() {
        return categoriaRepository.findAll().stream()
                .map(categoria -> new LookupResponseDTO(categoria.getIdCategoria(), categoria.getNome()))
                .collect(Collectors.toList());
    }

    /**
     * Finds a category by its ID.
     *
     * @param id The ID of the category.
     * @return The category as a {@link LookupResponseDTO}.
     * @throws ResponseStatusException if the category is not found.
     */
    public LookupResponseDTO buscarCategoria(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada."));
        return new LookupResponseDTO(categoria.getIdCategoria(), categoria.getNome());
    }

    /**
     * Creates a new category.
     *
     * @param request The request DTO containing the category name.
     * @return The created category as a {@link LookupResponseDTO}.
     */
    public LookupResponseDTO criarCategoria(LookupRequestDTO request) {
        Categoria categoria = new Categoria();
        categoria.setNome(validarNome(request.getNome()));
        Categoria salvo = categoriaRepository.save(categoria);
        return new LookupResponseDTO(salvo.getIdCategoria(), salvo.getNome());
    }

    /**
     * Updates an existing category.
     *
     * @param id      The ID of the category to update.
     * @param request The request DTO containing the new name.
     * @return The updated category as a {@link LookupResponseDTO}.
     * @throws ResponseStatusException if the category is not found.
     */
    public LookupResponseDTO atualizarCategoria(Integer id, LookupRequestDTO request) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada."));
        categoria.setNome(validarNome(request.getNome()));
        Categoria salvo = categoriaRepository.save(categoria);
        return new LookupResponseDTO(salvo.getIdCategoria(), salvo.getNome());
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id The ID of the category to delete.
     * @throws ResponseStatusException if the category is not found or is in use.
     */
    public void removerCategoria(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada."));
        removerEntidade(() -> categoriaRepository.delete(categoria), "Não foi possível remover a categoria pois ela está em uso.");
    }

    /**
     * Lists all priorities.
     *
     * @return A list of all priorities as {@link LookupResponseDTO}.
     */
    public List<LookupResponseDTO> listarPrioridades() {
        return prioridadeRepository.findAll().stream()
                .map(prioridade -> new LookupResponseDTO(prioridade.getIdPrioridade(), prioridade.getNome()))
                .collect(Collectors.toList());
    }

    /**
     * Finds a priority by its ID.
     *
     * @param id The ID of the priority.
     * @return The priority as a {@link LookupResponseDTO}.
     * @throws ResponseStatusException if the priority is not found.
     */
    public LookupResponseDTO buscarPrioridade(Integer id) {
        Prioridade prioridade = prioridadeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prioridade não encontrada."));
        return new LookupResponseDTO(prioridade.getIdPrioridade(), prioridade.getNome());
    }

    /**
     * Creates a new priority.
     *
     * @param request The request DTO containing the priority name.
     * @return The created priority as a {@link LookupResponseDTO}.
     */
    public LookupResponseDTO criarPrioridade(LookupRequestDTO request) {
        Prioridade prioridade = new Prioridade();
        prioridade.setNome(validarNome(request.getNome()));
        Prioridade salvo = prioridadeRepository.save(prioridade);
        return new LookupResponseDTO(salvo.getIdPrioridade(), salvo.getNome());
    }

    /**
     * Updates an existing priority.
     *
     * @param id      The ID of the priority to update.
     * @param request The request DTO containing the new name.
     * @return The updated priority as a {@link LookupResponseDTO}.
     * @throws ResponseStatusException if the priority is not found.
     */
    public LookupResponseDTO atualizarPrioridade(Integer id, LookupRequestDTO request) {
        Prioridade prioridade = prioridadeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prioridade não encontrada."));
        prioridade.setNome(validarNome(request.getNome()));
        Prioridade salvo = prioridadeRepository.save(prioridade);
        return new LookupResponseDTO(salvo.getIdPrioridade(), salvo.getNome());
    }

    /**
     * Deletes a priority by its ID.
     *
     * @param id The ID of the priority to delete.
     * @throws ResponseStatusException if the priority is not found or is in use.
     */
    public void removerPrioridade(Integer id) {
        Prioridade prioridade = prioridadeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prioridade não encontrada."));
        removerEntidade(() -> prioridadeRepository.delete(prioridade), "Não foi possível remover a prioridade pois ela está em uso.");
    }

    /**
     * Lists all statuses.
     *
     * @return A list of all statuses as {@link LookupResponseDTO}.
     */
    public List<LookupResponseDTO> listarStatus() {
        return statusRepository.findAll().stream()
                .map(status -> new LookupResponseDTO(status.getIdStatus(), status.getNome()))
                .collect(Collectors.toList());
    }

    /**
     * Finds a status by its ID.
     *
     * @param id The ID of the status.
     * @return The status as a {@link LookupResponseDTO}.
     * @throws ResponseStatusException if the status is not found.
     */
    public LookupResponseDTO buscarStatus(Integer id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status não encontrado."));
        return new LookupResponseDTO(status.getIdStatus(), status.getNome());
    }

    /**
     * Creates a new status.
     *
     * @param request The request DTO containing the status name.
     * @return The created status as a {@link LookupResponseDTO}.
     */
    public LookupResponseDTO criarStatus(LookupRequestDTO request) {
        Status status = new Status();
        status.setNome(validarNome(request.getNome()));
        Status salvo = statusRepository.save(status);
        return new LookupResponseDTO(salvo.getIdStatus(), salvo.getNome());
    }

    /**
     * Updates an existing status.
     *
     * @param id      The ID of the status to update.
     * @param request The request DTO containing the new name.
     * @return The updated status as a {@link LookupResponseDTO}.
     * @throws ResponseStatusException if the status is not found.
     */
    public LookupResponseDTO atualizarStatus(Integer id, LookupRequestDTO request) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status não encontrado."));
        status.setNome(validarNome(request.getNome()));
        Status salvo = statusRepository.save(status);
        return new LookupResponseDTO(salvo.getIdStatus(), salvo.getNome());
    }

    /**
     * Deletes a status by its ID.
     *
     * @param id The ID of the status to delete.
     * @throws ResponseStatusException if the status is not found or is in use.
     */
    public void removerStatus(Integer id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status não encontrado."));
        removerEntidade(() -> statusRepository.delete(status), "Não foi possível remover o status pois ele está em uso.");
    }

    /**
     * Lists all profiles.
     *
     * @return A list of all profiles as {@link LookupResponseDTO}.
     */
    public List<LookupResponseDTO> listarPerfis() {
        return perfilRepository.findAll().stream()
                .map(perfil -> new LookupResponseDTO(perfil.getIdPerfil(), perfil.getNome()))
                .collect(Collectors.toList());
    }

    /**
     * Finds a profile by its ID.
     *
     * @param id The ID of the profile.
     * @return The profile as a {@link LookupResponseDTO}.
     * @throws ResponseStatusException if the profile is not found.
     */
    public LookupResponseDTO buscarPerfil(Integer id) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil não encontrado."));
        return new LookupResponseDTO(perfil.getIdPerfil(), perfil.getNome());
    }

    /**
     * Creates a new profile.
     *
     * @param request The request DTO containing the profile name.
     * @return The created profile as a {@link LookupResponseDTO}.
     */
    public LookupResponseDTO criarPerfil(LookupRequestDTO request) {
        Perfil perfil = new Perfil();
        perfil.setNome(validarNome(request.getNome()));
        Perfil salvo = perfilRepository.save(perfil);
        return new LookupResponseDTO(salvo.getIdPerfil(), salvo.getNome());
    }

    /**
     * Updates an existing profile.
     *
     * @param id      The ID of the profile to update.
     * @param request The request DTO containing the new name.
     * @return The updated profile as a {@link LookupResponseDTO}.
     * @throws ResponseStatusException if the profile is not found.
     */
    public LookupResponseDTO atualizarPerfil(Integer id, LookupRequestDTO request) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil não encontrado."));
        perfil.setNome(validarNome(request.getNome()));
        Perfil salvo = perfilRepository.save(perfil);
        return new LookupResponseDTO(salvo.getIdPerfil(), salvo.getNome());
    }

    /**
     * Deletes a profile by its ID.
     *
     * @param id The ID of the profile to delete.
     * @throws ResponseStatusException if the profile is not found or is in use.
     */
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
