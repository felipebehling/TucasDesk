package com.example.Tucasdesk.dtos;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO used to confirm a password reset with the provided token.
 */
public class PasswordResetConfirmationDTO {

    @NotBlank(message = "O token é obrigatório.")
    private String token;

    @NotBlank(message = "Informe a nova senha.")
    private String novaSenha;

    @NotBlank(message = "Confirme a nova senha.")
    private String confirmacaoSenha;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getConfirmacaoSenha() {
        return confirmacaoSenha;
    }

    public void setConfirmacaoSenha(String confirmacaoSenha) {
        this.confirmacaoSenha = confirmacaoSenha;
    }

    public boolean hasMatchingPasswords() {
        return novaSenha != null && novaSenha.equals(confirmacaoSenha);
    }
}
