package com.example.Tucasdesk.dtos;

import java.util.Objects;

/**
 * Data Transfer Object (DTO) used when creating a new {@code Chamado}.
 */
public class ChamadoRequest {

    private String titulo;
    private String descricao;
    private Integer categoriaId;
    private Integer prioridadeId;
    private Integer statusId;
    private Integer usuarioId;
    private Integer tecnicoId;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Integer getPrioridadeId() {
        return prioridadeId;
    }

    public void setPrioridadeId(Integer prioridadeId) {
        this.prioridadeId = prioridadeId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getTecnicoId() {
        return tecnicoId;
    }

    public void setTecnicoId(Integer tecnicoId) {
        this.tecnicoId = tecnicoId;
    }

    /**
     * Validates that the mandatory fields are present.
     *
     * @throws IllegalArgumentException when mandatory data is missing.
     */
    public void validate() {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("O título do chamado é obrigatório.");
        }
        if (Objects.isNull(usuarioId)) {
            throw new IllegalArgumentException("O usuário solicitante é obrigatório.");
        }
        if (Objects.isNull(statusId)) {
            throw new IllegalArgumentException("O status inicial do chamado é obrigatório.");
        }
    }
}
