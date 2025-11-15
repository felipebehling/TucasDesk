package com.example.Tucasdesk.messaging.payload;

import com.example.Tucasdesk.model.Chamado;
import com.example.Tucasdesk.model.Usuario;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Payload emitted when a ticket is created.
 */
public class TicketCreatedEventPayload implements TicketEventPayload {

    public static final String EVENT_TYPE = "TicketCreated";

    private final String eventType;
    private final Integer chamadoId;
    private final String titulo;
    private final String descricao;
    private final Integer solicitanteId;
    private final Integer tecnicoId;
    private final Integer categoriaId;
    private final Integer prioridadeId;
    private final Integer statusId;
    private final LocalDateTime dataAbertura;

    public TicketCreatedEventPayload(String eventType,
                                     Integer chamadoId,
                                     String titulo,
                                     String descricao,
                                     Integer solicitanteId,
                                     Integer tecnicoId,
                                     Integer categoriaId,
                                     Integer prioridadeId,
                                     Integer statusId,
                                     LocalDateTime dataAbertura) {
        this.eventType = eventType;
        this.chamadoId = chamadoId;
        this.titulo = titulo;
        this.descricao = descricao;
        this.solicitanteId = solicitanteId;
        this.tecnicoId = tecnicoId;
        this.categoriaId = categoriaId;
        this.prioridadeId = prioridadeId;
        this.statusId = statusId;
        this.dataAbertura = dataAbertura;
    }

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

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public Integer getChamadoId() {
        return chamadoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getSolicitanteId() {
        return solicitanteId;
    }

    public Integer getTecnicoId() {
        return tecnicoId;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public Integer getPrioridadeId() {
        return prioridadeId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }
}
