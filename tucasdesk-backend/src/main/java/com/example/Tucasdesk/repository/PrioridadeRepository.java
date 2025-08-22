package com.example.Tucasdesk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Tucasdesk.model.Prioridade;

@Repository
public interface PrioridadeRepository extends JpaRepository<Prioridade, Integer> {
    Optional<Prioridade> findByNome(String nome);
}
