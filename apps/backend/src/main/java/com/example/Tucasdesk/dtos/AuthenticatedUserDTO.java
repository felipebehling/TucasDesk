package com.example.Tucasdesk.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Data Transfer Object (DTO) representing the authenticated user information that
 * should be exposed to the frontend.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticatedUserDTO {

    private final Integer id;
    private final String nome;
    private final String email;
    private final String role;

    public AuthenticatedUserDTO(Integer id, String nome, String email, String role) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
