package com.example.Tucasdesk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Tucasdesk.model.Usuario;

/**
 * Repository interface for {@link Usuario} entities.
 * Provides standard CRUD operations and custom query methods for accessing user data.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    /**
     * Finds a user by their name.
     *
     * @param nome The name of the user to find.
     * @return An {@link Optional} containing the user if found, or empty otherwise.
     */
    Optional<Usuario> findByNome(String nome);

    /**
     * Finds a user by their email address.
     *
     * @param email The email address of the user to find.
     * @return An {@link Optional} containing the user if found, or empty otherwise.
     */
    Optional<Usuario> findByEmail(String email);
}
