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

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        log.warn("event=api_resource_not_found status={} path={} message=\"{}\"", status.value(), request.getDescription(false), ex.getMessage());
        return ResponseEntity.status(status).body(new ErrorResponseDTO(ex.getMessage(), status.name()));
    }

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

    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponseDTO> handleBadRequest(Exception ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.warn("event=api_bad_request status={} message=\"{}\"", status.value(), ex.getMessage(), ex);
        return ResponseEntity.status(status)
                .body(new ErrorResponseDTO(Optional.ofNullable(ex.getMessage()).orElse("Requisição inválida."), status.name()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("event=api_unexpected_error status={} path={} message=\"{}\"", status.value(), request.getDescription(false), ex.getMessage(), ex);
        return ResponseEntity.status(status)
                .body(new ErrorResponseDTO("Ocorreu um erro interno inesperado.", status.name()));
    }
}
