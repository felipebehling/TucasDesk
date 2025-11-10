package com.example.Tucasdesk.messaging.notifier;

/**
 * Immutable representation of a notification to be sent by the notifier service.
 *
 * @param subject human friendly summary displayed to the recipient.
 * @param body    detailed description containing contextual data for the event.
 */
public record NotificationMessage(String subject, String body) {
}
