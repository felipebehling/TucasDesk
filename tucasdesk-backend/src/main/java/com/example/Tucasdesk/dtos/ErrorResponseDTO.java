package com.example.Tucasdesk.dtos;

import java.time.Instant;

/**
 * DTO representing a standardized error payload returned by the API.
 */
public class ErrorResponseDTO {

    private final String message;
    private final String code;
    private final Instant timestamp;

    public ErrorResponseDTO(String message, String code) {
        this.message = message;
        this.code = code;
        this.timestamp = Instant.now();
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
