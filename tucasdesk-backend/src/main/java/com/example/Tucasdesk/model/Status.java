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

    // getters e setters
    public Integer getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Integer idStatus) {
        this.idStatus = idStatus;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
