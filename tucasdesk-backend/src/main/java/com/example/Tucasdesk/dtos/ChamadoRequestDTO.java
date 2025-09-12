package com.example.Tucasdesk.dtos;

import com.example.Tucasdesk.model.Categoria;
import com.example.Tucasdesk.model.Prioridade;
import com.example.Tucasdesk.model.Usuario;

public class ChamadoRequestDTO {

    private String titulo;
    private String descricao;
    private Categoria categoria;
    private Prioridade prioridade;
    private Usuario usuario;

    // Getters and Setters
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
