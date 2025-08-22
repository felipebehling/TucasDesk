package com.example.Tucasdesk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Tucasdesk.model.Perfil;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
    Optional<Perfil> findByNome(String nome);
}
