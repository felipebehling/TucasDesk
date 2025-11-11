package com.example.Tucasdesk.dtos;

/**
 * Lightweight DTO exposing the essential information from a {@code Usuario}.
 */
public class UsuarioResumoDTO {

    private Integer id;
    private String nome;
    private String email;

    public UsuarioResumoDTO() {
    }

    public UsuarioResumoDTO(Integer id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
