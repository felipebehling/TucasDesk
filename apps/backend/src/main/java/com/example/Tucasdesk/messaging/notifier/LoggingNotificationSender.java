package com.example.Tucasdesk.messaging.notifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Default notification sender that logs the notification payload.
 * <p>
 * It acts as a safe placeholder until a real delivery mechanism (for example AWS SES)
 * is integrated with the application.
 * </p>
 */
@Component
@ConditionalOnProperty(prefix = "app.aws.ses", name = "enabled", havingValue = "false", matchIfMissing = true)
public class LoggingNotificationSender implements NotificationSender {

    private static final Logger log = LoggerFactory.getLogger(LoggingNotificationSender.class);

    @Override
    public void send(NotificationMessage message) {
        log.info("event=notifier_notification_sent subject={} body={} templateModel={}",
                message.getSubject(), message.getBody(), message.getTemplateModel());
    }
}
