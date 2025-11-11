package com.example.Tucasdesk.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Data Transfer Object (DTO) for sending responses after a successful login attempt.
 * It contains the authentication token information and the authenticated user details
 * required by the frontend to build the session state.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDTO {

    private final String token;
    private final String accessToken;
    private final String refreshToken;
    private final AuthenticatedUserDTO usuario;

    public LoginResponseDTO(String token, String refreshToken, AuthenticatedUserDTO usuario) {
        this(token, null, refreshToken, usuario);
    }

    public LoginResponseDTO(String token, String accessToken, String refreshToken, AuthenticatedUserDTO usuario) {
        this.token = token;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public AuthenticatedUserDTO getUsuario() {
        return usuario;
    }
}
