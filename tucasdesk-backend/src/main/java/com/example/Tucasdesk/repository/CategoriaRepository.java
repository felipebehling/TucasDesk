package com.example.Tucasdesk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Tucasdesk.model.Categoria;

/**
 * Repository interface for {@link Categoria} entities.
 * Provides standard CRUD operations and custom query methods for accessing category data.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    /**
     * Finds a category by its name.
     *
     * @param nome The name of the category to find.
     * @return An {@link Optional} containing the category if found, or empty otherwise.
     */
    Optional<Categoria> findByNome(String nome);
}
