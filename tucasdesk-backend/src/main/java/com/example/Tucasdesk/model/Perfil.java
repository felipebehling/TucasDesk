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
    
    /**
     * Gets the unique identifier for the profile.
     * @return The unique identifier for the profile.
     */
    public Integer getIdPerfil() {
        return idPerfil;
    }

    /**
     * Sets the unique identifier for the profile.
     * @param idPerfil The unique identifier for the profile.
     */
    public void setIdPerfil(Integer idPerfil) {
        this.idPerfil = idPerfil;
    }

    /**
     * Gets the name of the profile.
     * @return The name of the profile.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Sets the name of the profile.
     * @param nome The name of the profile.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
}
