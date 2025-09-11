package com.example.Tucasdesk.model;

import jakarta.persistence.*;

/**
 * Represents a user profile or role in the system (e.g., Administrator, Technician, User).
 * This entity is mapped to the "perfis" table in the database.
 */
@Entity
@Table(name = "perfis")
public class Perfil {
    /**
     * The unique identifier for the profile.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPerfil;

    /**
     * The name of the profile (e.g., "Admin", "User").
     */
    private String nome;
    
    // getters e setters
    public Integer getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Integer idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
}
