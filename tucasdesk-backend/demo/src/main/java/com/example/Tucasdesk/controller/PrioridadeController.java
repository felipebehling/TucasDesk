package com.example.Tucasdesk.controller;

import org.springframework.web.bind.annotation.*;

import com.example.Tucasdesk.model.Prioridade;
import com.example.Tucasdesk.repository.PrioridadeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/Prioridade")
public class PrioridadeController {

    @Autowired
    private PrioridadeRepository PrioridadeRepository;

    @GetMapping
    public List<Prioridade> listarTodos() {
        return PrioridadeRepository.findAll();
    }

    @PostMapping
    public Prioridade criar(@RequestBody Prioridade Prioridade) {
        return PrioridadeRepository.save(Prioridade);
    }
}
