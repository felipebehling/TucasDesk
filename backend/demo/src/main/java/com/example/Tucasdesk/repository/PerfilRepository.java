package com.example.Tucasdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Tucasdesk.model.Perfil;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
    List<Perfil> findAll();

    Perfil save(Perfil Perfil);
}
