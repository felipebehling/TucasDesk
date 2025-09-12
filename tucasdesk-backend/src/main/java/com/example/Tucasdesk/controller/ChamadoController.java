package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.dtos.ChamadoRequestDTO;
import com.example.Tucasdesk.model.Chamado;
import com.example.Tucasdesk.model.Status;
import com.example.Tucasdesk.repository.ChamadoRepository;
import com.example.Tucasdesk.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for handling ticket-related requests.
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/chamados")
public class ChamadoController {

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Autowired
    private StatusRepository statusRepository;

    /**
     * Retrieves a list of all tickets.
     *
     * @return A list of all {@link Chamado} objects.
     */
    @GetMapping
    public List<Chamado> listarTodos() {
        return chamadoRepository.findAll();
    }

    /**
     * Creates a new ticket.
     *
     * @param chamadoRequestDTO The DTO containing the ticket information.
     * @return The created {@link Chamado} object.
     */
    @PostMapping
    public Chamado criar(@RequestBody ChamadoRequestDTO chamadoRequestDTO) {
        Chamado chamado = new Chamado();
        chamado.setTitulo(chamadoRequestDTO.getTitulo());
        chamado.setDescricao(chamadoRequestDTO.getDescricao());
        chamado.setCategoria(chamadoRequestDTO.getCategoria());
        chamado.setPrioridade(chamadoRequestDTO.getPrioridade());
        chamado.setUsuario(chamadoRequestDTO.getUsuario());

        chamado.setDataAbertura(LocalDateTime.now());

        Status statusAberto = statusRepository.findByNome("Aberto")
                .orElseThrow(() -> new RuntimeException("Status 'Aberto' not found"));
        chamado.setStatus(statusAberto);

        return chamadoRepository.save(chamado);
    }
}
