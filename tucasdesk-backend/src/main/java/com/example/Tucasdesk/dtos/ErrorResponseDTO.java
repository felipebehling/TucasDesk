package com.example.Tucasdesk.dtos;

/**
 * DTO representing an error response returned by the authentication endpoints.
 */
public class ErrorResponseDTO {

    private final String message;

    public ErrorResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
