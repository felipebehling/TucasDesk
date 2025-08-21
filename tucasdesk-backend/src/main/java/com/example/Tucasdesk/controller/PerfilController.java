package com.example.Tucasdesk.controller;

import org.springframework.web.bind.annotation.*;

import com.example.Tucasdesk.model.Perfil;
import com.example.Tucasdesk.repository.PerfilRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/Perfis")
public class PerfilController {

    @Autowired
    private PerfilRepository PerfilRepository;

    @GetMapping
    public List<Perfil> listarTodos() {
        return PerfilRepository.findAll();
    }

    @PostMapping
    public Perfil criar(@RequestBody Perfil Perfil) {
        return PerfilRepository.save(Perfil);
    }
}
