package com.example.Tucasdesk.messaging.notifier;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Immutable representation of a notification to be sent by the notifier service.
 *
 * @param subject       human friendly summary displayed to the recipient.
 * @param body          detailed description containing contextual data for the event.
 * @param templateModel data model that can be interpolated into an e-mail template.
 */
public record NotificationMessage(String subject, String body, Map<String, Object> templateModel) {

    public NotificationMessage {
        templateModel = templateModel != null ? Collections.unmodifiableMap(new LinkedHashMap<>(templateModel)) : Map.of();
    }
}
