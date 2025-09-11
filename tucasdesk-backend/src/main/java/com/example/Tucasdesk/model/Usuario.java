package com.example.Tucasdesk.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a user in the system.
 * This entity is mapped to the "usuarios" table in the database.
 */
@Entity
@Table(name = "usuarios")
public class Usuario {
    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    /**
     * The name of the user.
     */
    private String nome;

    /**
     * The email address of the user. This is used for login and communication.
     */
    private String email;

    /**
     * The password for the user's account.
     */
    private String senha;

    /**
     * The profile associated with the user, which defines their role and permissions.
     */
    @ManyToOne
    @JoinColumn(name = "perfil_id")
    private Perfil perfil;

    /**
     * The timestamp when the user account was created.
     */
    private LocalDateTime dataCriacao;

    /**
     * A flag indicating whether the user account is active.
     */
    private Boolean ativo;
    
    // getters e setters
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

}
