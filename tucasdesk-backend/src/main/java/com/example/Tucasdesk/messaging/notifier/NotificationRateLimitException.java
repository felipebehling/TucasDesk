package com.example.Tucasdesk.messaging.notifier;

/**
 * Exception raised when the downstream provider refuses delivery due to rate limiting.
 */
public class NotificationRateLimitException extends NotificationDeliveryException {

    public NotificationRateLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
