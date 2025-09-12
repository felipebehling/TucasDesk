package com.example.Tucasdesk.model;

import jakarta.persistence.*;

/**
 * Represents the status of a support ticket (e.g., Open, In Progress, Closed).
 * This entity is mapped to the "status" table in the database.
 */
@Entity
@Table(name = "status")
public class Status {
    /**
     * The unique identifier for the status.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idStatus;

    /**
     * The name of the status.
     */
    private String nome;

    /**
     * Gets the unique identifier for the status.
     * @return The unique identifier for the status.
     */
    public Integer getIdStatus() {
        return idStatus;
    }

    /**
     * Sets the unique identifier for the status.
     * @param idStatus The unique identifier for the status.
     */
    public void setIdStatus(Integer idStatus) {
        this.idStatus = idStatus;
    }

    /**
     * Gets the name of the status.
     * @return The name of the status.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Sets the name of the status.
     * @param nome The name of the status.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

}
