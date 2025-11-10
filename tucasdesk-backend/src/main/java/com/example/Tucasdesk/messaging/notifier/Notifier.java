package com.example.Tucasdesk.messaging.notifier;

import com.example.Tucasdesk.messaging.ChamadoEventPayload;
import com.example.Tucasdesk.messaging.ChamadoMessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Service responsible for transforming ticket events into user-facing notifications
 * and delegating their delivery to a {@link NotificationSender} implementation.
 */
@Service
public class Notifier {

    private static final Logger log = LoggerFactory.getLogger(Notifier.class);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            .withLocale(Locale.forLanguageTag("pt-BR"));

    private final NotificationSender sender;

    public Notifier(NotificationSender sender) {
        this.sender = sender;
    }

    /**
     * Consumes a {@link ChamadoEventPayload} received from SQS and orchestrates the delivery of a notification.
     *
     * @param payload event consumed from the queue.
     */
    public void notify(ChamadoEventPayload payload) {
        if (payload == null) {
            log.warn("event=notifier_payload_null");
            return;
        }
        NotificationMessage message = buildMessage(payload);
        try {
            sender.send(message);
        } catch (RuntimeException ex) {
            log.error("event=notifier_delivery_failed chamadoId={} eventType={} message={}",
                    payload.chamadoId(), payload.eventType(), ex.getMessage(), ex);
            throw ex;
        }
    }

    NotificationMessage buildMessage(ChamadoEventPayload payload) {
        String subject = buildSubject(payload);
        String body = buildBody(payload);
        return new NotificationMessage(subject, body);
    }

    private String buildSubject(ChamadoEventPayload payload) {
        String titulo = StringUtils.hasText(payload.titulo()) ? payload.titulo() : "Sem título";
        return switch (payload.eventType()) {
            case ChamadoMessagingService.EVENT_CHAMADO_CREATED ->
                    "Chamado #%d criado: %s".formatted(payload.chamadoId(), titulo);
            case ChamadoMessagingService.EVENT_CHAMADO_UPDATED ->
                    "Chamado #%d atualizado".formatted(payload.chamadoId());
            case ChamadoMessagingService.EVENT_CHAMADO_STATUS_CHANGED ->
                    "Status atualizado para o chamado #%d".formatted(payload.chamadoId());
            case ChamadoMessagingService.EVENT_CHAMADO_INTERACAO_ADDED ->
                    "Nova interação no chamado #%d".formatted(payload.chamadoId());
            default -> "Atualização no chamado #%d".formatted(payload.chamadoId());
        };
    }

    private String buildBody(ChamadoEventPayload payload) {
        StringBuilder body = new StringBuilder();
        body.append("Evento: ").append(payload.eventType()).append('\n');
        body.append("Chamado: #").append(payload.chamadoId()).append('\n');
        if (StringUtils.hasText(payload.titulo())) {
            body.append("Título: ").append(payload.titulo()).append('\n');
        }
        if (StringUtils.hasText(payload.descricao())) {
            body.append("Descrição: ").append(payload.descricao()).append('\n');
        }
        if (payload.dataAbertura() != null) {
            body.append("Aberto em: ").append(DATE_FORMATTER.format(payload.dataAbertura())).append('\n');
        }
        if (payload.dataFechamento() != null) {
            body.append("Fechado em: ").append(DATE_FORMATTER.format(payload.dataFechamento())).append('\n');
        }
        if (payload.interacao() != null) {
            body.append('\n').append("Interação #").append(payload.interacao().interacaoId()).append('\n');
            if (payload.interacao().usuarioId() != null) {
                body.append("Usuário: ").append(payload.interacao().usuarioId()).append('\n');
            }
            if (StringUtils.hasText(payload.interacao().mensagem())) {
                body.append("Mensagem: ").append(payload.interacao().mensagem()).append('\n');
            }
            if (payload.interacao().dataInteracao() != null) {
                body.append("Data: ").append(DATE_FORMATTER.format(payload.interacao().dataInteracao())).append('\n');
            }
            if (StringUtils.hasText(payload.interacao().anexoUrl())) {
                body.append("Anexo: ").append(payload.interacao().anexoUrl()).append('\n');
            }
        }
        return body.toString();
    }
}
