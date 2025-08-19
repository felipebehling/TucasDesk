package com.example.Tucasdesk.controller;

import org.springframework.web.bind.annotation.*;

import com.example.Tucasdesk.model.Chamado;
import com.example.Tucasdesk.repository.ChamadoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api/Chamados")
public class ChamadoController {

    @Autowired
    private ChamadoRepository ChamadoRepository;

    @GetMapping
    public List<Chamado> listarTodos() {
        return ChamadoRepository.findAll();
    }

    @PostMapping
    public Chamado criar(@RequestBody Chamado Chamado) {
        return ChamadoRepository.save(Chamado);
    }
}
