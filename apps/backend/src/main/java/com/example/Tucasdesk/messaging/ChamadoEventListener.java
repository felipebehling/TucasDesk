package com.example.Tucasdesk.messaging;

import com.example.Tucasdesk.messaging.notifier.Notifier;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Component responsible for consuming messages from the configured SQS queue.
 */
@Component
@ConditionalOnExpression("T(org.springframework.util.StringUtils).hasText('${app.aws.messaging.queue-name:}')")
public class ChamadoEventListener {

    private static final Logger log = LoggerFactory.getLogger(ChamadoEventListener.class);

    private final Notifier notifier;

    public ChamadoEventListener(Notifier notifier) {
        this.notifier = notifier;
    }

    @SqsListener("${app.aws.messaging.queue-name}")
    public void onChamadoEvent(ChamadoEventPayload payload,
                               @Header(name = "ApproximateReceiveCount", required = false) String receiveCount) {
        int attempts = parseAttempts(receiveCount);
        if (payload == null) {
            log.warn("event=chamado_message_received payload=null attempts={}", attempts);
            return;
        }
        log.info("event=chamado_message_received type={} chamadoId={} usuarioId={} tecnicoId={} attempts={}",
                payload.getEventType(),
                payload.getChamadoId(),
                payload.getUsuarioId(),
                payload.getTecnicoId(),
                attempts);
        if (payload.getInteracao() != null) {
            log.info("event=chamado_interacao_received chamadoId={} interacaoId={} usuarioId={} attempts={}",
                    payload.getChamadoId(),
                    payload.getInteracao().getInteracaoId(),
                    payload.getInteracao().getUsuarioId(),
                    attempts);
        }
        try {
            notifier.notify(payload);
        } catch (RuntimeException ex) {
            log.error("event=chamado_notification_error chamadoId={} attempts={} message={}",
                    payload.getChamadoId(), attempts, ex.getMessage(), ex);
            throw ex;
        }
    }

    private int parseAttempts(String receiveCount) {
        if (!StringUtils.hasText(receiveCount)) {
            return 1;
        }
        try {
            return Integer.parseInt(receiveCount);
        } catch (NumberFormatException ex) {
            log.warn("event=chamado_receive_count_invalid value={}", receiveCount, ex);
            return 1;
        }
    }
}
