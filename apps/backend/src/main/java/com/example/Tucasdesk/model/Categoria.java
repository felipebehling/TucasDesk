package com.example.Tucasdesk.model;

import jakarta.persistence.*;

/**
 * Represents a category for a support ticket (e.g., Hardware, Software, Network).
 * This entity is mapped to the "categorias" table in the database.
 */
@Entity
@Table(name = "categorias")
public class Categoria {
    /**
     * The unique identifier for the category.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCategoria;

    /**
     * The name of the category.
     */
    private String nome;
    
    /**
     * Gets the unique identifier for the category.
     * @return The unique identifier for the category.
     */
    public Integer getIdCategoria() {
        return idCategoria;
    }

    /**
     * Sets the unique identifier for the category.
     * @param idCategoria The unique identifier for the category.
     */
    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Gets the name of the category.
     * @return The name of the category.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Sets the name of the category.
     * @param nome The name of the category.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    
}
