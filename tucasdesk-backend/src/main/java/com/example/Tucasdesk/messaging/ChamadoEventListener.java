package com.example.Tucasdesk.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import io.awspring.cloud.sqs.annotation.SqsListener;

/**
 * Component responsible for consuming messages from the configured SQS queue.
 */
@Component
@ConditionalOnExpression("T(org.springframework.util.StringUtils).hasText('${app.aws.messaging.queue-name:}')")
public class ChamadoEventListener {

    private static final Logger log = LoggerFactory.getLogger(ChamadoEventListener.class);

    @SqsListener("${app.aws.messaging.queue-name}")
    public void onChamadoEvent(ChamadoEventPayload payload) {
        if (payload == null) {
            log.warn("event=chamado_message_received payload=null");
            return;
        }
        log.info("event=chamado_message_received type={} chamadoId={} usuarioId={} tecnicoId={}",
                payload.eventType(),
                payload.chamadoId(),
                payload.usuarioId(),
                payload.tecnicoId());
        if (payload.interacao() != null) {
            log.info("event=chamado_interacao_received chamadoId={} interacaoId={} usuarioId={}",
                    payload.chamadoId(),
                    payload.interacao().interacaoId(),
                    payload.interacao().usuarioId());
        }
    }
}
