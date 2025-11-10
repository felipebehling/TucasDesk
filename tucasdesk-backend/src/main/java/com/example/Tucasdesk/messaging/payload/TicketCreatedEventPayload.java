package com.example.Tucasdesk.messaging.payload;

import com.example.Tucasdesk.model.Chamado;
import com.example.Tucasdesk.model.Usuario;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Payload emitted when a ticket is created.
 */
public record TicketCreatedEventPayload(
        String eventType,
        Integer chamadoId,
        String titulo,
        String descricao,
        Integer solicitanteId,
        Integer tecnicoId,
        Integer categoriaId,
        Integer prioridadeId,
        Integer statusId,
        LocalDateTime dataAbertura
) implements TicketEventPayload {

    public static final String EVENT_TYPE = "TicketCreated";

    /**
     * Maps the provided entity into a message payload.
     *
     * @param chamado the ticket created in the platform.
     * @return the resulting payload.
     */
    public static TicketCreatedEventPayload fromChamado(Chamado chamado) {
        return new TicketCreatedEventPayload(
                EVENT_TYPE,
                chamado.getIdChamado(),
                chamado.getTitulo(),
                chamado.getDescricao(),
                getUsuarioId(chamado.getUsuario()),
                getUsuarioId(chamado.getTecnico()),
                chamado.getCategoria() != null ? chamado.getCategoria().getIdCategoria() : null,
                chamado.getPrioridade() != null ? chamado.getPrioridade().getIdPrioridade() : null,
                chamado.getStatus() != null ? chamado.getStatus().getIdStatus() : null,
                chamado.getDataAbertura()
        );
    }

    private static Integer getUsuarioId(Usuario usuario) {
        return Objects.nonNull(usuario) ? usuario.getIdUsuario() : null;
    }
}
