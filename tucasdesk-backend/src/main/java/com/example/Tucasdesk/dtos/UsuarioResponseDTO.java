package com.example.Tucasdesk.dtos;

import com.example.Tucasdesk.model.Perfil;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for sending user data in API responses.
 * This DTO excludes sensitive information like the user's password.
 */
public class UsuarioResponseDTO {

    /**
     * The unique identifier for the user.
     */
    private Integer idUsuario;
    /**
     * The name of the user.
     */
    private String nome;
    /**
     * The email address of the user.
     */
    private String email;
    /**
     * The profile of the user, which defines their role.
     */
    private Perfil perfil;
    /**
     * The timestamp when the user account was created.
     */
    private LocalDateTime dataCriacao;
    /**
     * A flag indicating whether the user account is active.
     */
    private Boolean ativo;

    /**
     * Constructs a new UsuarioResponseDTO.
     *
     * @param idUsuario   The unique identifier for the user.
     * @param nome        The name of the user.
     * @param email       The email address of the user.
     * @param perfil      The profile of the user.
     * @param dataCriacao The timestamp of account creation.
     * @param ativo       The active status of the user account.
     */
    public UsuarioResponseDTO(Integer idUsuario, String nome, String email, Perfil perfil, LocalDateTime dataCriacao, Boolean ativo) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo;
    }

    /**
     * Gets the unique identifier for the user.
     * @return The unique identifier for the user.
     */
    public Integer getIdUsuario() {
        return idUsuario;
    }

    /**
     * Sets the unique identifier for the user.
     * @param idUsuario The unique identifier for the user.
     */
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Gets the name of the user.
     * @return The name of the user.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Sets the name of the user.
     * @param nome The name of the user.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Gets the email address of the user.
     * @return The email address of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     * @param email The email address of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the profile of the user.
     * @return The profile of the user.
     */
    public Perfil getPerfil() {
        return perfil;
    }

    /**
     * Sets the profile of the user.
     * @param perfil The profile of the user.
     */
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    /**
     * Gets the timestamp when the user account was created.
     * @return The timestamp of account creation.
     */
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    /**
     * Sets the timestamp when the user account was created.
     * @param dataCriacao The timestamp of account creation.
     */
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    /**
     * Gets the active status of the user account.
     * @return The active status of the user account.
     */
    public Boolean getAtivo() {
        return ativo;
    }

    /**
     * Sets the active status of the user account.
     * @param ativo The active status of the user account.
     */
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
