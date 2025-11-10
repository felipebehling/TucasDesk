package com.example.Tucasdesk.messaging.notifier;

/**
 * Contract for components capable of delivering a {@link NotificationMessage} to recipients.
 */
@FunctionalInterface
public interface NotificationSender {

    /**
     * Sends a notification to the configured channel.
     *
     * @param message notification to be delivered.
     */
    void send(NotificationMessage message);
}
