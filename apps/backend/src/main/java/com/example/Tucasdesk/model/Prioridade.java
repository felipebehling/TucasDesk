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
    
    /**
     * Gets the unique identifier for the priority level.
     * @return The unique identifier for the priority level.
     */
    public Integer getIdPrioridade() {
        return idPrioridade;
    }

    /**
     * Sets the unique identifier for the priority level.
     * @param idPrioridade The unique identifier for the priority level.
     */
    public void setIdPrioridade(Integer idPrioridade) {
        this.idPrioridade = idPrioridade;
    }

    /**
     * Gets the name of the priority level.
     * @return The name of the priority level.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Sets the name of the priority level.
     * @param nome The name of the priority level.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
}
