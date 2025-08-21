package com.example.Tucasdesk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Tucasdesk.model.Categoria;
import com.example.Tucasdesk.model.Chamado;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Integer> {
    Optional<Categoria> findByNome(String nome);
}
