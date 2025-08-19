package com.example.Tucasdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Tucasdesk.model.Interacao;

@Repository
public interface InteracaoRepository extends JpaRepository<Interacao, Integer> {
    List<Interacao> findAll();

    Interacao save(Interacao Interacao);
}
