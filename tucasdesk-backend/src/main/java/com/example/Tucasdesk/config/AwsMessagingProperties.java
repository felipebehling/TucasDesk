package com.example.Tucasdesk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for application specific AWS messaging settings.
 */
@ConfigurationProperties(prefix = "app.aws.messaging")
public class AwsMessagingProperties {

    /**
     * SNS topic ARN that will receive ticket related notifications.
     */
    private String topicArn;

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

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}
