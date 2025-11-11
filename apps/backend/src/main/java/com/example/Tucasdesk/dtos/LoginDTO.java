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

    /**
     * Gets the user's email address.
     *
     * @return The user's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email The user's email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user's password.
     *
     * @return The user's password.
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Sets the user's password.
     *
     * @param senha The user's password.
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }
}
