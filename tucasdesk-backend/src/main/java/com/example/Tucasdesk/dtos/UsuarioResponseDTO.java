package com.example.Tucasdesk.dtos;

import com.example.Tucasdesk.model.Perfil;
import java.time.LocalDateTime;

public class UsuarioResponseDTO {

    private Integer idUsuario;
    private String nome;
    private String email;
    private Perfil perfil;
    private LocalDateTime dataCriacao;
    private Boolean ativo;

    public UsuarioResponseDTO(Integer idUsuario, String nome, String email, Perfil perfil, LocalDateTime dataCriacao, Boolean ativo) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo;
    }

    // Getters and Setters
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
