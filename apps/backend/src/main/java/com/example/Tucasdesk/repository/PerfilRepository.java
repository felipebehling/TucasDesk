package com.example.Tucasdesk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Tucasdesk.model.Perfil;

/**
 * Repository interface for {@link Perfil} entities.
 * Provides standard CRUD operations and custom query methods for accessing profile data.
 */
@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
    /**
     * Finds a profile by its name.
     *
     * @param nome The name of the profile to find.
     * @return An {@link Optional} containing the profile if found, or empty otherwise.
     */
    Optional<Perfil> findByNome(String nome);

    /**
     * Finds a profile by its name ignoring casing.
     *
     * @param nome The name of the profile to find.
     * @return An {@link Optional} containing the profile if found, or empty otherwise.
     */
    Optional<Perfil> findByNomeIgnoreCase(String nome);
}
