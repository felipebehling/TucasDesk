package com.example.Tucasdesk.dtos;

import java.time.LocalDateTime;

/**
 * DTO representing an entry of the status change timeline for a ticket.
 */
public class HistoricoStatusResponseDTO {

    private LookupResponseDTO status;
    private LocalDateTime dataRegistro;

    public HistoricoStatusResponseDTO() {
    }

    public HistoricoStatusResponseDTO(LookupResponseDTO status, LocalDateTime dataRegistro) {
        this.status = status;
        this.dataRegistro = dataRegistro;
    }

    public LookupResponseDTO getStatus() {
        return status;
    }

    public void setStatus(LookupResponseDTO status) {
        this.status = status;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}
