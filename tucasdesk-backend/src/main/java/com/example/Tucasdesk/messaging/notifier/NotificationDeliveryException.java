package com.example.Tucasdesk.messaging.notifier;

/**
 * Runtime exception thrown when the notification cannot be delivered to the destination channel.
 */
public class NotificationDeliveryException extends RuntimeException {

    public NotificationDeliveryException(String message) {
        super(message);
    }

    public NotificationDeliveryException(String message, Throwable cause) {
        super(message, cause);
    }
}
