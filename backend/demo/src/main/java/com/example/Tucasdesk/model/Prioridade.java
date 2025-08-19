package com.example.Tucasdesk.model;

import jakarta.persistence.*;

@Entity
@Table(name = "prioridade")
public class Prioridade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPrioridade;

    private String nome;
    
    // getters e setters
    public Integer getIdPrioridade() {
        return idPrioridade;
    }

    public void setIdPrioridade(Integer idPrioridade) {
        this.idPrioridade = idPrioridade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
}
