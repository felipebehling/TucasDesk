package com.example.Tucasdesk.dtos;

import jakarta.validation.constraints.NotBlank;

/**
 * Request payload used to renew authentication tokens via AWS Cognito.
 */
public class RefreshTokenRequest {

    @NotBlank(message = "Informe um refresh token válido.")
    private String refreshToken;

    public RefreshTokenRequest() {
    }

    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
