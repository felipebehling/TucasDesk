package com.example.Tucasdesk.dtos;

/**
 * Generic DTO used to expose lightweight lookup entities such as categoria, status or prioridade.
 */
public class LookupResponseDTO {

    private Integer id;
    private String nome;

    public LookupResponseDTO() {
    }

    public LookupResponseDTO(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
