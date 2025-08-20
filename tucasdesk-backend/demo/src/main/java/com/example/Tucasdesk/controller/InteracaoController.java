package com.example.Tucasdesk.controller;

import org.springframework.web.bind.annotation.*;

import com.example.Tucasdesk.model.Interacao;
import com.example.Tucasdesk.repository.InteracaoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api/Interacoes")
public class InteracaoController {

    @Autowired
    private InteracaoRepository InteracaoRepository;

    @GetMapping
    public List<Interacao> listarTodos() {
        return InteracaoRepository.findAll();
    }

    @PostMapping
    public Interacao criar(@RequestBody Interacao Interacao) {
        return InteracaoRepository.save(Interacao);
    }
}
