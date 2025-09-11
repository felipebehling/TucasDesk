package com.example.Tucasdesk.model;

import jakarta.persistence.*;

/**
 * Represents the priority level of a support ticket (e.g., Low, Medium, High).
 * This entity is mapped to the "prioridade" table in the database.
 */
@Entity
@Table(name = "prioridade")
public class Prioridade {
    /**
     * The unique identifier for the priority level.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPrioridade;

    /**
     * The name of the priority level.
     */
    private String nome;
    
    // getters e setters
    public Integer getIdPrioridade() {
        return idPrioridade;
    }

    public void setIdPrioridade(Integer idPrioridade) {
        this.idPrioridade = idPrioridade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
}
