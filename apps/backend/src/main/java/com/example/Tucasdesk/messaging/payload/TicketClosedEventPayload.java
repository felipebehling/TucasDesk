package com.example.Tucasdesk.messaging.payload;

import com.example.Tucasdesk.model.Chamado;
import com.example.Tucasdesk.model.Usuario;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Payload emitted when a ticket transitions to the closed state.
 */
public class TicketClosedEventPayload implements TicketEventPayload {

    public static final String EVENT_TYPE = "TicketClosed";

    private final String eventType;
    private final Integer chamadoId;
    private final Integer solicitanteId;
    private final Integer tecnicoId;
    private final Integer statusId;
    private final LocalDateTime dataFechamento;

    public TicketClosedEventPayload(String eventType,
                                    Integer chamadoId,
                                    Integer solicitanteId,
                                    Integer tecnicoId,
                                    Integer statusId,
                                    LocalDateTime dataFechamento) {
        this.eventType = eventType;
        this.chamadoId = chamadoId;
        this.solicitanteId = solicitanteId;
        this.tecnicoId = tecnicoId;
        this.statusId = statusId;
        this.dataFechamento = dataFechamento;
    }

    public static TicketClosedEventPayload fromChamado(Chamado chamado) {
        return new TicketClosedEventPayload(
                EVENT_TYPE,
                chamado.getIdChamado(),
                getUsuarioId(chamado.getUsuario()),
                getUsuarioId(chamado.getTecnico()),
                chamado.getStatus() != null ? chamado.getStatus().getIdStatus() : null,
                chamado.getDataFechamento()
        );
    }

    private static Integer getUsuarioId(Usuario usuario) {
        return Objects.nonNull(usuario) ? usuario.getIdUsuario() : null;
    }

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public Integer getChamadoId() {
        return chamadoId;
    }

    public Integer getSolicitanteId() {
        return solicitanteId;
    }

    public Integer getTecnicoId() {
        return tecnicoId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }
}
