package com.example.Tucasdesk.messaging.notifier;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Immutable representation of a notification to be sent by the notifier service.
 */
public class NotificationMessage {

    private final String subject;
    private final String body;
    private final Map<String, Object> templateModel;

    public NotificationMessage(String subject, String body, Map<String, Object> templateModel) {
        this.subject = subject;
        this.body = body;
        if (templateModel != null) {
            this.templateModel = Collections.unmodifiableMap(new LinkedHashMap<>(templateModel));
        } else {
            this.templateModel = Collections.emptyMap();
        }
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public Map<String, Object> getTemplateModel() {
        return templateModel;
    }
}
