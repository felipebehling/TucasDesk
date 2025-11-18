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

    public String getTopicArn() {
        return topicArn;
    }

    public void setTopicArn(String topicArn) {
        this.topicArn = topicArn;
    }

    public Topics getTopics() {
        return topics;
    }

    public String getQueueName() {
        return queueName;
    }

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

        public String getTicketCreatedArn() {
            return ticketCreatedArn;
        }

        public void setTicketCreatedArn(String ticketCreatedArn) {
            this.ticketCreatedArn = ticketCreatedArn;
        }

        public String getTicketClosedArn() {
            return ticketClosedArn;
        }

        public void setTicketClosedArn(String ticketClosedArn) {
            this.ticketClosedArn = ticketClosedArn;
        }

        public String getTicketInteractedArn() {
            return ticketInteractedArn;
        }

        public void setTicketInteractedArn(String ticketInteractedArn) {
            this.ticketInteractedArn = ticketInteractedArn;
        }
    }
}
