package com.example.Tucasdesk.messaging;

import com.example.Tucasdesk.config.AwsMessagingProperties;
import com.example.Tucasdesk.model.Chamado;
import com.example.Tucasdesk.model.Interacao;
import io.awspring.cloud.sns.core.SnsTemplate;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Facade responsible for publishing ticket related events to SNS topics and SQS queues.
 */
@Service
public class ChamadoMessagingService {

    private static final Logger log = LoggerFactory.getLogger(ChamadoMessagingService.class);

    public static final String EVENT_CHAMADO_CREATED = "CHAMADO_CREATED";
    public static final String EVENT_CHAMADO_UPDATED = "CHAMADO_UPDATED";
    public static final String EVENT_CHAMADO_STATUS_CHANGED = "CHAMADO_STATUS_CHANGED";
    public static final String EVENT_CHAMADO_INTERACAO_ADDED = "CHAMADO_INTERACAO_ADDED";

    private final SnsTemplate snsTemplate;
    private final SqsTemplate sqsTemplate;
    private final AwsMessagingProperties properties;

    public ChamadoMessagingService(SnsTemplate snsTemplate,
                                   SqsTemplate sqsTemplate,
                                   AwsMessagingProperties properties) {
        this.snsTemplate = snsTemplate;
        this.sqsTemplate = sqsTemplate;
        this.properties = properties;
    }

    /**
     * Sends a message stating that a ticket was created.
     *
     * @param chamado ticket that triggered the event.
     */
    public void publishChamadoCreatedEvent(Chamado chamado) {
        send(ChamadoEventPayload.fromChamado(EVENT_CHAMADO_CREATED, chamado));
    }

    /**
     * Sends a message stating that a ticket was updated.
     *
     * @param chamado ticket that triggered the event.
     */
    public void publishChamadoUpdatedEvent(Chamado chamado) {
        send(ChamadoEventPayload.fromChamado(EVENT_CHAMADO_UPDATED, chamado));
    }

    /**
     * Sends a message stating that a ticket had its status changed.
     *
     * @param chamado ticket that triggered the event.
     */
    public void publishChamadoStatusChangedEvent(Chamado chamado) {
        send(ChamadoEventPayload.fromChamado(EVENT_CHAMADO_STATUS_CHANGED, chamado));
    }

    /**
     * Sends a message stating that a new interaction was added to a ticket.
     *
     * @param chamado   ticket that triggered the event.
     * @param interacao interaction saved for the ticket.
     */
    public void publishChamadoInteracaoAddedEvent(Chamado chamado, Interacao interacao) {
        send(ChamadoEventPayload.fromInteracao(EVENT_CHAMADO_INTERACAO_ADDED, chamado, interacao));
    }

    private void send(ChamadoEventPayload payload) {
        boolean hasTargets = false;
        if (StringUtils.hasText(properties.getTopicArn())) {
            hasTargets = true;
            try {
                snsTemplate.convertAndSend(properties.getTopicArn(), payload);
                log.debug("event=chamado_message_sns_published type={} chamadoId={} topic={}",
                        payload.eventType(), payload.chamadoId(), properties.getTopicArn());
            } catch (Exception ex) {
                log.error("event=chamado_message_sns_error type={} chamadoId={} topic={} message={}",
                        payload.eventType(), payload.chamadoId(), properties.getTopicArn(), ex.getMessage(), ex);
            }
        }
        if (StringUtils.hasText(properties.getQueueName())) {
            hasTargets = true;
            try {
                sqsTemplate.convertAndSend(properties.getQueueName(), payload);
                log.debug("event=chamado_message_sqs_published type={} chamadoId={} queue={}",
                        payload.eventType(), payload.chamadoId(), properties.getQueueName());
            } catch (Exception ex) {
                log.error("event=chamado_message_sqs_error type={} chamadoId={} queue={} message={}",
                        payload.eventType(), payload.chamadoId(), properties.getQueueName(), ex.getMessage(), ex);
            }
        }
        if (!hasTargets) {
            log.debug("event=chamado_message_skipped reason=no_targets_configured type={} chamadoId={}",
                    payload.eventType(), payload.chamadoId());
        }
    }
}
