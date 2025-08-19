package com.example.Tucasdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Tucasdesk.model.Chamado;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Integer> {
    List<Chamado> findAll();

    Chamado save(Chamado Chamado);
}
