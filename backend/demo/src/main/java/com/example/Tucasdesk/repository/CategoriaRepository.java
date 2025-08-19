package com.example.Tucasdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Tucasdesk.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    List<Categoria> findAll();

    Categoria save(Categoria Categoria);
}
