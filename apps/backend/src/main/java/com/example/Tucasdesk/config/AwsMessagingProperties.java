package com.example.Tucasdesk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for application specific AWS messaging settings.
 */
@ConfigurationProperties(prefix = "app.aws.messaging")
public class AwsMessagingProperties {

    /**
     * General fallback topic ARN used when a specific mapping is not provided.
     */
    private String topicArn;

    /**
     * Collection of ticket related topics.
     */
    private final Topics topics = new Topics();

    /**
     * SQS queue name subscribed to the SNS topic or used directly by the application.
     */
    private String queueName;

    /**
     * @return The general fallback topic ARN.
     */
    public String getTopicArn() {
        return topicArn;
    }

    /**
     * @param topicArn The general fallback topic ARN.
     */
    public void setTopicArn(String topicArn) {
        this.topicArn = topicArn;
    }

    /**
     * @return The nested topic mappings.
     */
    public Topics getTopics() {
        return topics;
    }

    /**
     * @return The SQS queue name.
     */
    public String getQueueName() {
        return queueName;
    }

    /**
     * @param queueName The SQS queue name.
     */
    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    /**
     * Nested configuration that maps ticket events to dedicated SNS topics.
     */
    public static class Topics {

        /**
         * Topic ARN used for {@code TicketCreated} notifications.
         */
        private String ticketCreatedArn;

        /**
         * Topic ARN used for {@code TicketClosed} notifications.
         */
        private String ticketClosedArn;

        /**
         * Topic ARN used for {@code TicketInteracted} notifications.
         */
        private String ticketInteractedArn;

        /**
         * @return The ARN for 'ticket created' events.
         */
        public String getTicketCreatedArn() {
            return ticketCreatedArn;
        }

        /**
         * @param ticketCreatedArn The ARN for 'ticket created' events.
         */
        public void setTicketCreatedArn(String ticketCreatedArn) {
            this.ticketCreatedArn = ticketCreatedArn;
        }

        /**
         * @return The ARN for 'ticket closed' events.
         */
        public String getTicketClosedArn() {
            return ticketClosedArn;
        }

        /**
         * @param ticketClosedArn The ARN for 'ticket closed' events.
         */
        public void setTicketClosedArn(String ticketClosedArn) {
            this.ticketClosedArn = ticketClosedArn;
        }

        /**
         * @return The ARN for 'ticket interacted' events.
         */
        public String getTicketInteractedArn() {
            return ticketInteractedArn;
        }

        /**
         * @param ticketInteractedArn The ARN for 'ticket interacted' events.
         */
        public void setTicketInteractedArn(String ticketInteractedArn) {
            this.ticketInteractedArn = ticketInteractedArn;
        }
    }
}
