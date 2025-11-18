package com.example.Tucasdesk.config;

import com.example.Tucasdesk.dtos.ErrorResponseDTO;
import com.example.Tucasdesk.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Global exception handler responsible for intercepting exceptions thrown by the controllers
 * and returning a standardized error payload.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    /**
     * Handles exceptions thrown when a requested resource is not found.
     *
     * @param ex      The thrown {@link ResourceNotFoundException}.
     * @param request The current web request.
     * @return A {@link ResponseEntity} with a 404 Not Found status and a standardized error body.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        log.warn("event=api_resource_not_found status={} path={} message=\"{}\"", status.value(), request.getDescription(false), ex.getMessage());
        return ResponseEntity.status(status).body(new ErrorResponseDTO(ex.getMessage(), status.name()));
    }

    /**
     * Handles generic exceptions triggered by response status codes.
     *
     * @param ex      The thrown {@link ResponseStatusException}.
     * @param request The current web request.
     * @return A {@link ResponseEntity} with the corresponding HTTP status and a standardized error body.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDTO> handleResponseStatus(ResponseStatusException ex, WebRequest request) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        String message = Optional.ofNullable(ex.getReason()).orElse(status.getReasonPhrase());
        log.error("event=api_exception type=ResponseStatusException status={} path={} message=\"{}\"", status.value(), request.getDescription(false), message, ex);
        return ResponseEntity.status(status).body(new ErrorResponseDTO(message, status.name()));
    }

    /**
     * Handles exceptions from validation errors, such as @Valid annotations.
     *
     * @param ex The thrown {@link MethodArgumentNotValidException}.
     * @return A {@link ResponseEntity} with a 400 Bad Request status and a message from the validation error.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(error -> {
                    if (error instanceof FieldError) {
                        FieldError fieldError = (FieldError) error;
                        return fieldError.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                })
                .orElse("Requisição inválida.");
        log.warn("event=api_validation_error status={} message=\"{}\"", status.value(), message);
        return ResponseEntity.status(status).body(new ErrorResponseDTO(message, status.name()));
    }

    /**
     * Handles exceptions related to bad requests, such as illegal arguments or unreadable messages.
     *
     * @param ex The thrown exception (e.g., {@link IllegalArgumentException}).
     * @return A {@link ResponseEntity} with a 400 Bad Request status and a standardized error body.
     */
    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponseDTO> handleBadRequest(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.warn("event=api_bad_request status={} message=\"{}\"", status.value(), ex.getMessage(), ex);
        return ResponseEntity.status(status)
                .body(new ErrorResponseDTO(Optional.ofNullable(ex.getMessage()).orElse("Requisição inválida."), status.name()));
    }

    /**
     * Handles any other unexpected exceptions as a fallback.
     *
     * @param ex      The generic {@link Exception} thrown.
     * @param request The current web request.
     * @return A {@link ResponseEntity} with a 500 Internal Server Error status and a generic error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("event=api_unexpected_error status={} path={} message=\"{}\"", status.value(), request.getDescription(false), ex.getMessage(), ex);
        return ResponseEntity.status(status)
                .body(new ErrorResponseDTO("Ocorreu um erro interno inesperado.", status.name()));
    }
}
