package com.example.Tucasdesk.controller;

import org.springframework.web.bind.annotation.*;

import com.example.Tucasdesk.model.Categoria;
import com.example.Tucasdesk.repository.CategoriaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api/Categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository CategoriaRepository;

    @GetMapping
    public List<Categoria> listarTodos() {
        return CategoriaRepository.findAll();
    }

    @PostMapping
    public Categoria criar(@RequestBody Categoria Categoria) {
        return CategoriaRepository.save(Categoria);
    }
}
