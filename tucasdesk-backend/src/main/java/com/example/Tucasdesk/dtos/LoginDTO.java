package com.example.Tucasdesk.dtos;

/**
 * Data Transfer Object (DTO) for handling user login requests.
 * It encapsulates the credentials (email and password) sent by the client.
 */
public class LoginDTO {
    /**
     * The user's email address.
     */
    private String email;
    /**
     * The user's password.
     */
    private String senha;

    // getters e setters
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
}
