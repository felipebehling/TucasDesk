package com.example.Tucasdesk.messaging.payload;

import com.example.Tucasdesk.model.Chamado;
import com.example.Tucasdesk.model.Usuario;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Payload emitted when a ticket transitions to the closed state.
 */
public record TicketClosedEventPayload(
        String eventType,
        Integer chamadoId,
        Integer solicitanteId,
        Integer tecnicoId,
        Integer statusId,
        LocalDateTime dataFechamento
) implements TicketEventPayload {

    public static final String EVENT_TYPE = "TicketClosed";

    /**
     * Maps the provided entity into a message payload.
     *
     * @param chamado the ticket that has been closed.
     * @return the resulting payload.
     */
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
}
