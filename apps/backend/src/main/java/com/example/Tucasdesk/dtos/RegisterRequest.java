package com.example.Tucasdesk.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.AssertTrue;

/**
 * DTO used to register a new user.
 */
public class RegisterRequest {

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O e-mail informado é inválido.")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 8, message = "A senha deve conter pelo menos 8 caracteres.")
    private String senha;

    @NotBlank(message = "A confirmação da senha é obrigatória.")
    private String confirmacaoSenha;

    @AssertTrue(message = "A confirmação da senha deve coincidir com a senha informada.")
    public boolean isConfirmacaoValida() {
        if (senha == null || confirmacaoSenha == null) {
            return false;
        }
        return senha.equals(confirmacaoSenha);
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getConfirmacaoSenha() {
        return confirmacaoSenha;
    }

    public void setConfirmacaoSenha(String confirmacaoSenha) {
        this.confirmacaoSenha = confirmacaoSenha;
    }
}
