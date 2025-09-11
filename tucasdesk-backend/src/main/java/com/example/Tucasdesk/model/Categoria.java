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
    
    // getters e setters
    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    
}
