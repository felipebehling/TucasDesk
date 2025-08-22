package com.example.Tucasdesk.dtos;

public class LoginResponseDTO {
    private String token;
    private String username;
    private String message;

    public LoginResponseDTO(String token, String username, String message) {
        this.token = token;
        this.username = username;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
