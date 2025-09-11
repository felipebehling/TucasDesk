package com.example.Tucasdesk.dtos;

/**
 * Data Transfer Object (DTO) for sending responses after a login attempt.
 * It includes a JWT, the username, and a status message.
 */
public class LoginResponseDTO {
    /**
     * The JWT generated upon successful authentication.
     */
    private String token;
    /**
     * The name of the authenticated user.
     */
    private String username;
    /**
     * A message indicating the result of the login attempt (e.g., "Login successful").
     */
    private String message;

    /**
     * Constructs a new LoginResponseDTO.
     *
     * @param token    The authentication token.
     * @param username The name of the user.
     * @param message  A response message.
     */
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
