package com.example.Tucasdesk.dtos;

import jakarta.validation.constraints.Size;

/**
 * DTO used to update an existing {@code Chamado}.
 */
public class ChamadoUpdateRequest {

    @Size(min = 1, max = 255, message = "O título deve ter entre 1 e 255 caracteres.")
    private String titulo;
    private String descricao;
    private Integer categoriaId;
    private Integer prioridadeId;
    private Integer statusId;
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

    public Integer getTecnicoId() {
        return tecnicoId;
    }

    public void setTecnicoId(Integer tecnicoId) {
        this.tecnicoId = tecnicoId;
    }
}
