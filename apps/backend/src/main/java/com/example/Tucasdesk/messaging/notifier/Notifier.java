package com.example.Tucasdesk.messaging.notifier;

import com.example.Tucasdesk.messaging.ChamadoEventPayload;
import com.example.Tucasdesk.messaging.ChamadoMessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

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
                    payload.getChamadoId(), payload.getEventType(), ex.getMessage(), ex);
            throw ex;
        }
    }

    NotificationMessage buildMessage(ChamadoEventPayload payload) {
        String subject = buildSubject(payload);
        String body = buildBody(payload);
        Map<String, Object> templateModel = buildTemplateModel(payload, subject, body);
        return new NotificationMessage(subject, body, templateModel);
    }

    private String buildSubject(ChamadoEventPayload payload) {
        String titulo = StringUtils.hasText(payload.getTitulo()) ? payload.getTitulo() : "Sem título";
        String eventType = payload.getEventType();
        if (ChamadoMessagingService.EVENT_CHAMADO_CREATED.equals(eventType)) {
            return String.format("Chamado #%d criado: %s", payload.getChamadoId(), titulo);
        }
        if (ChamadoMessagingService.EVENT_CHAMADO_UPDATED.equals(eventType)) {
            return String.format("Chamado #%d atualizado", payload.getChamadoId());
        }
        if (ChamadoMessagingService.EVENT_CHAMADO_STATUS_CHANGED.equals(eventType)) {
            return String.format("Status atualizado para o chamado #%d", payload.getChamadoId());
        }
        if (ChamadoMessagingService.EVENT_CHAMADO_INTERACAO_ADDED.equals(eventType)) {
            return String.format("Nova interação no chamado #%d", payload.getChamadoId());
        }
        return String.format("Atualização no chamado #%d", payload.getChamadoId());
    }

    private String buildBody(ChamadoEventPayload payload) {
        StringBuilder body = new StringBuilder();
        body.append("Evento: ").append(payload.getEventType()).append('\n');
        body.append("Chamado: #").append(payload.getChamadoId()).append('\n');
        if (StringUtils.hasText(payload.getTitulo())) {
            body.append("Título: ").append(payload.getTitulo()).append('\n');
        }
        if (StringUtils.hasText(payload.getDescricao())) {
            body.append("Descrição: ").append(payload.getDescricao()).append('\n');
        }
        if (payload.getDataAbertura() != null) {
            body.append("Aberto em: ").append(DATE_FORMATTER.format(payload.getDataAbertura())).append('\n');
        }
        if (payload.getDataFechamento() != null) {
            body.append("Fechado em: ").append(DATE_FORMATTER.format(payload.getDataFechamento())).append('\n');
        }
        if (payload.getInteracao() != null) {
            body.append('\n').append("Interação #").append(payload.getInteracao().getInteracaoId()).append('\n');
            if (payload.getInteracao().getUsuarioId() != null) {
                body.append("Usuário: ").append(payload.getInteracao().getUsuarioId()).append('\n');
            }
            if (StringUtils.hasText(payload.getInteracao().getMensagem())) {
                body.append("Mensagem: ").append(payload.getInteracao().getMensagem()).append('\n');
            }
            if (payload.getInteracao().getDataInteracao() != null) {
                body.append("Data: ").append(DATE_FORMATTER.format(payload.getInteracao().getDataInteracao())).append('\n');
            }
            if (StringUtils.hasText(payload.getInteracao().getAnexoUrl())) {
                body.append("Anexo: ").append(payload.getInteracao().getAnexoUrl()).append('\n');
            }
        }
        return body.toString();
    }

    private Map<String, Object> buildTemplateModel(ChamadoEventPayload payload, String subject, String body) {
        Map<String, Object> model = new LinkedHashMap<>();
        model.put("subject", subject);
        model.put("body", body);
        model.put("eventType", payload.getEventType());
        model.put("ticketId", payload.getChamadoId());
        if (StringUtils.hasText(payload.getTitulo())) {
            model.put("titulo", payload.getTitulo());
        }
        if (StringUtils.hasText(payload.getDescricao())) {
            model.put("descricao", payload.getDescricao());
        }
        if (payload.getDataAbertura() != null) {
            model.put("dataAbertura", DATE_FORMATTER.format(payload.getDataAbertura()));
        }
        if (payload.getDataFechamento() != null) {
            model.put("dataFechamento", DATE_FORMATTER.format(payload.getDataFechamento()));
        }
        if (payload.getInteracao() != null) {
            Map<String, Object> interaction = new LinkedHashMap<>();
            interaction.put("interacaoId", payload.getInteracao().getInteracaoId());
            if (payload.getInteracao().getUsuarioId() != null) {
                interaction.put("usuarioId", payload.getInteracao().getUsuarioId());
            }
            if (StringUtils.hasText(payload.getInteracao().getMensagem())) {
                interaction.put("mensagem", payload.getInteracao().getMensagem());
            }
            if (payload.getInteracao().getDataInteracao() != null) {
                interaction.put("dataInteracao", DATE_FORMATTER.format(payload.getInteracao().getDataInteracao()));
            }
            if (StringUtils.hasText(payload.getInteracao().getAnexoUrl())) {
                interaction.put("anexoUrl", payload.getInteracao().getAnexoUrl());
            }
            model.put("interacao", interaction);
        }
        return model;
    }
}
