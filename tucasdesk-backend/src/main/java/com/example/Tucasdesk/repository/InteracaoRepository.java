package com.example.Tucasdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Tucasdesk.model.Interacao;

@Repository
public interface InteracaoRepository extends JpaRepository<Interacao, Integer> {
}
