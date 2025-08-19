package com.example.Tucasdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Tucasdesk.model.Prioridade;

@Repository
public interface PrioridadeRepository extends JpaRepository<Prioridade, Integer> {
    List<Prioridade> findAll();

    Prioridade save(Prioridade Prioridade);
}
