package com.example.Tucasdesk.messaging;

import com.example.Tucasdesk.model.Chamado;
import com.example.Tucasdesk.model.Interacao;
import com.example.Tucasdesk.model.Usuario;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Immutable payload sent through SNS/SQS for ticket related events.
 */
public class ChamadoEventPayload {

    private final String eventType;
    private final Integer chamadoId;
    private final String titulo;
    private final String descricao;
    private final Integer usuarioId;
    private final Integer tecnicoId;
    private final Integer categoriaId;
    private final Integer prioridadeId;
    private final Integer statusId;
    private final LocalDateTime dataAbertura;
    private final LocalDateTime dataFechamento;
    private final InteracaoPayload interacao;

    public ChamadoEventPayload(String eventType,
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
                               InteracaoPayload interacao) {
        this.eventType = eventType;
        this.chamadoId = chamadoId;
        this.titulo = titulo;
        this.descricao = descricao;
        this.usuarioId = usuarioId;
        this.tecnicoId = tecnicoId;
        this.categoriaId = categoriaId;
        this.prioridadeId = prioridadeId;
        this.statusId = statusId;
        this.dataAbertura = dataAbertura;
        this.dataFechamento = dataFechamento;
        this.interacao = interacao;
    }

    public String getEventType() {
        return eventType;
    }

    public Integer getChamadoId() {
        return chamadoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getUsuarioId() {
        return usuarioId;
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

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    public InteracaoPayload getInteracao() {
        return interacao;
    }

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
    public static final class InteracaoPayload {

        private final Integer interacaoId;
        private final Integer usuarioId;
        private final String mensagem;
        private final LocalDateTime dataInteracao;
        private final String anexoUrl;

        public InteracaoPayload(Integer interacaoId,
                                Integer usuarioId,
                                String mensagem,
                                LocalDateTime dataInteracao,
                                String anexoUrl) {
            this.interacaoId = interacaoId;
            this.usuarioId = usuarioId;
            this.mensagem = mensagem;
            this.dataInteracao = dataInteracao;
            this.anexoUrl = anexoUrl;
        }

        public Integer getInteracaoId() {
            return interacaoId;
        }

        public Integer getUsuarioId() {
            return usuarioId;
        }

        public String getMensagem() {
            return mensagem;
        }

        public LocalDateTime getDataInteracao() {
            return dataInteracao;
        }

        public String getAnexoUrl() {
            return anexoUrl;
        }
    }
}
