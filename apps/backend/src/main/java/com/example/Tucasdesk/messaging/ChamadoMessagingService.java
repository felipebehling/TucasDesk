package com.example.Tucasdesk.messaging;

import com.example.Tucasdesk.config.AwsMessagingProperties;
import com.example.Tucasdesk.messaging.payload.TicketClosedEventPayload;
import com.example.Tucasdesk.messaging.payload.TicketCreatedEventPayload;
import com.example.Tucasdesk.messaging.payload.TicketEventPayload;
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
    private final com.example.Tucasdesk.leader.LeaderElectionService leaderElectionService;

    public ChamadoMessagingService(SnsTemplate snsTemplate,
                                   SqsTemplate sqsTemplate,
                                   AwsMessagingProperties properties,
                                   com.example.Tucasdesk.leader.LeaderElectionService leaderElectionService) {
        this.snsTemplate = snsTemplate;
        this.sqsTemplate = sqsTemplate;
        this.properties = properties;
        this.leaderElectionService = leaderElectionService;
    }

    /**
     * Sends a message stating that a ticket was created.
     *
     * @param chamado ticket that triggered the event.
     */
    public void publishChamadoCreatedEvent(Chamado chamado) {
        if (!leaderElectionService.isLeader()) {
            return;
        }
        String topicArn = properties.getTopics().getTicketCreatedArn();
        if (StringUtils.hasText(topicArn)) {
            TicketCreatedEventPayload payload = TicketCreatedEventPayload.fromChamado(chamado);
            if (sendTicketEvent(payload, topicArn)) {
                sendQueue(ChamadoEventPayload.fromChamado(EVENT_CHAMADO_CREATED, chamado));
            }
            return;
        }
        send(ChamadoEventPayload.fromChamado(EVENT_CHAMADO_CREATED, chamado));
    }

    /**
     * Sends a message stating that a ticket was closed.
     *
     * @param chamado ticket that triggered the event.
     */
    public void publishChamadoClosedEvent(Chamado chamado) {
        if (!leaderElectionService.isLeader()) {
            return;
        }
        String topicArn = properties.getTopics().getTicketClosedArn();
        if (StringUtils.hasText(topicArn)) {
            TicketClosedEventPayload payload = TicketClosedEventPayload.fromChamado(chamado);
            sendTicketEvent(payload, topicArn);
            return;
        }
        log.debug("event=chamado_ticket_closed_fallback reason=topic_not_configured chamadoId={}",
                chamado.getIdChamado());
    }

    /**
     * Sends a message stating that a ticket was updated.
     *
     * @param chamado ticket that triggered the event.
     */
    public void publishChamadoUpdatedEvent(Chamado chamado) {
        if (!leaderElectionService.isLeader()) {
            return;
        }
        send(ChamadoEventPayload.fromChamado(EVENT_CHAMADO_UPDATED, chamado));
    }

    /**
     * Sends a message stating that a ticket had its status changed.
     *
     * @param chamado ticket that triggered the event.
     */
    public void publishChamadoStatusChangedEvent(Chamado chamado) {
        if (!leaderElectionService.isLeader()) {
            return;
        }
        send(ChamadoEventPayload.fromChamado(EVENT_CHAMADO_STATUS_CHANGED, chamado));
    }

    /**
     * Sends a message stating that a new interaction was added to a ticket.
     *
     * @param chamado   ticket that triggered the event.
     * @param interacao interaction saved for the ticket.
     */
    public void publishChamadoInteracaoAddedEvent(Chamado chamado, Interacao interacao) {
        if (!leaderElectionService.isLeader()) {
            return;
        }
        String topicArn = properties.getTopics().getTicketInteractedArn();
        if (StringUtils.hasText(topicArn)) {
            // Placeholder for a specific TicketInteractedEventPayload if needed
            sendToTopic(topicArn, ChamadoEventPayload.fromInteracao(EVENT_CHAMADO_INTERACAO_ADDED, chamado, interacao), EVENT_CHAMADO_INTERACAO_ADDED, chamado.getIdChamado());
            return;
        }
        send(ChamadoEventPayload.fromInteracao(EVENT_CHAMADO_INTERACAO_ADDED, chamado, interacao));
    }

    private boolean sendTicketEvent(TicketEventPayload payload, String topicArn) {
        boolean delivered = sendToTopic(topicArn, payload, payload.getEventType(), payload.getChamadoId());
        if (!delivered) {
            log.debug("event=chamado_message_skipped reason=no_topic_configured type={} chamadoId={}",
                    payload.getEventType(), payload.getChamadoId());
        }
        return delivered;
    }

    private void send(ChamadoEventPayload payload) {
        boolean hasTargets = false;
        hasTargets = sendToTopic(properties.getTopicArn(), payload, payload.getEventType(), payload.getChamadoId()) || hasTargets;
        hasTargets = sendQueue(payload) || hasTargets;
        if (!hasTargets) {
            log.debug("event=chamado_message_skipped reason=no_targets_configured type={} chamadoId={}",
                    payload.getEventType(), payload.getChamadoId());
        }
    }

    private boolean sendQueue(Object payload) {
        if (StringUtils.hasText(properties.getQueueName())) {
            try {
                sqsTemplate.send(properties.getQueueName(), payload);
                log.debug("event=chamado_message_sqs_published payloadType={} queue={}",
                        payload.getClass().getSimpleName(), properties.getQueueName());
            } catch (Exception ex) {
                log.error("event=chamado_message_sqs_error payloadType={} queue={} message={}",
                        payload.getClass().getSimpleName(), properties.getQueueName(), ex.getMessage(), ex);
            }
            return true;
        }
        return false;
    }

    private boolean sendToTopic(String topicArn, Object payload, String eventType, Integer chamadoId) {
        if (StringUtils.hasText(topicArn)) {
            try {
                snsTemplate.convertAndSend(topicArn, payload);
                log.debug("event=chamado_message_sns_published type={} chamadoId={} topic={}",
                        eventType, chamadoId, topicArn);
            } catch (Exception ex) {
                log.error("event=chamado_message_sns_error type={} chamadoId={} topic={} message={}",
                        eventType, chamadoId, topicArn, ex.getMessage(), ex);
            }
            return true;
        }
        return false;
    }
}
