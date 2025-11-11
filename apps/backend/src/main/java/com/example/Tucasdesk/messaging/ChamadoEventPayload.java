package com.example.Tucasdesk.messaging;

import com.example.Tucasdesk.model.Chamado;
import com.example.Tucasdesk.model.Interacao;
import com.example.Tucasdesk.model.Usuario;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Immutable payload sent through SNS/SQS for ticket related events.
 */
public record ChamadoEventPayload(
        String eventType,
        Integer chamadoId,
        String titulo,
        String descricao,
        Integer usuarioId,
        Integer tecnicoId,
        Integer categoriaId,
        Integer prioridadeId,
        Integer statusId,
        LocalDateTime dataAbertura,
        LocalDateTime dataFechamento,
        InteracaoPayload interacao
) {

    /**
     * Creates a payload describing the current state of the ticket.
     *
     * @param eventType the type of domain event that triggered the message.
     * @param chamado   the ticket entity used as source.
     * @return a populated payload instance.
     */
    public static ChamadoEventPayload fromChamado(String eventType, Chamado chamado) {
        return new ChamadoEventPayload(
                eventType,
                chamado.getIdChamado(),
                chamado.getTitulo(),
                chamado.getDescricao(),
                getUsuarioId(chamado.getUsuario()),
                getUsuarioId(chamado.getTecnico()),
                chamado.getCategoria() != null ? chamado.getCategoria().getIdCategoria() : null,
                chamado.getPrioridade() != null ? chamado.getPrioridade().getIdPrioridade() : null,
                chamado.getStatus() != null ? chamado.getStatus().getIdStatus() : null,
                chamado.getDataAbertura(),
                chamado.getDataFechamento(),
                null
        );
    }

    /**
     * Creates a payload containing the ticket snapshot and interaction data.
     *
     * @param eventType the type of domain event that triggered the message.
     * @param chamado   the ticket entity used as source.
     * @param interacao the interaction associated with the event.
     * @return a populated payload instance.
     */
    public static ChamadoEventPayload fromInteracao(String eventType, Chamado chamado, Interacao interacao) {
        return new ChamadoEventPayload(
                eventType,
                chamado.getIdChamado(),
                chamado.getTitulo(),
                chamado.getDescricao(),
                getUsuarioId(chamado.getUsuario()),
                getUsuarioId(chamado.getTecnico()),
                chamado.getCategoria() != null ? chamado.getCategoria().getIdCategoria() : null,
                chamado.getPrioridade() != null ? chamado.getPrioridade().getIdPrioridade() : null,
                chamado.getStatus() != null ? chamado.getStatus().getIdStatus() : null,
                chamado.getDataAbertura(),
                chamado.getDataFechamento(),
                interacao != null ? new InteracaoPayload(
                        interacao.getIdInteracao(),
                        getUsuarioId(interacao.getUsuario()),
                        interacao.getMensagem(),
                        interacao.getDataInteracao(),
                        interacao.getAnexoUrl()
                ) : null
        );
    }

    private static Integer getUsuarioId(Usuario usuario) {
        return Objects.nonNull(usuario) ? usuario.getIdUsuario() : null;
    }

    /**
     * Nested payload describing an interaction.
     *
     * @param interacaoId   identifier of the persisted interaction.
     * @param usuarioId     identifier of the user that created the interaction.
     * @param mensagem      textual description sent by the user.
     * @param dataInteracao timestamp when the interaction was registered.
     * @param anexoUrl      optional attachment URL included with the interaction.
     */
    public record InteracaoPayload(
            Integer interacaoId,
            Integer usuarioId,
            String mensagem,
            LocalDateTime dataInteracao,
            String anexoUrl
    ) {
    }
}
