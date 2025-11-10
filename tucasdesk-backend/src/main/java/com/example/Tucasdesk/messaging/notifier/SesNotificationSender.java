package com.example.Tucasdesk.messaging.notifier;

import com.example.Tucasdesk.config.AwsSesProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.Destination;
import software.amazon.awssdk.services.sesv2.model.EmailContent;
import software.amazon.awssdk.services.sesv2.model.SendEmailRequest;
import software.amazon.awssdk.services.sesv2.model.SendEmailResponse;
import software.amazon.awssdk.services.sesv2.model.SesV2Exception;
import software.amazon.awssdk.services.sesv2.model.Template;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Sends notifications using the AWS Simple Email Service, relying on pre-created templates.
 */
@Component
@ConditionalOnBean(SesV2Client.class)
@ConditionalOnProperty(prefix = "app.aws.ses", name = "enabled", havingValue = "true")
public class SesNotificationSender implements NotificationSender {

    private static final Logger log = LoggerFactory.getLogger(SesNotificationSender.class);

    private final SesV2Client sesClient;
    private final AwsSesProperties properties;
    private final ObjectMapper objectMapper;
    private final Counter successCounter;
    private final Counter failureCounter;
    private final Counter throttledCounter;

    public SesNotificationSender(SesV2Client sesClient,
                                 AwsSesProperties properties,
                                 ObjectMapper objectMapper,
                                 MeterRegistry meterRegistry) {
        this.sesClient = sesClient;
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.successCounter = meterRegistry.counter("notifier.ses.deliveries", "outcome", "success");
        this.failureCounter = meterRegistry.counter("notifier.ses.deliveries", "outcome", "failure");
        this.throttledCounter = meterRegistry.counter("notifier.ses.throttled");
    }

    @Override
    public void send(NotificationMessage message) {
        validateConfiguration();

        SendEmailRequest.Builder requestBuilder = SendEmailRequest.builder()
                .fromEmailAddress(properties.getFromAddress())
                .destination(buildDestination())
                .content(EmailContent.builder()
                        .template(Template.builder()
                                .templateName(properties.getTemplateName())
                                .templateData(buildTemplateData(message))
                                .build())
                        .build());

        if (properties.getReplyToAddress() != null && !properties.getReplyToAddress().isBlank()) {
            requestBuilder = requestBuilder.replyToAddresses(properties.getReplyToAddress());
        }
        if (properties.getConfigurationSetName() != null && !properties.getConfigurationSetName().isBlank()) {
            requestBuilder = requestBuilder.configurationSetName(properties.getConfigurationSetName());
        }

        try {
            SendEmailResponse response = sesClient.sendEmail(requestBuilder.build());
            successCounter.increment();
            log.info("event=ses_notification_sent messageId={}", response.messageId());
        } catch (SesV2Exception ex) {
            handleSesException(message, ex);
        } catch (AwsServiceException ex) {
            failureCounter.increment();
            String errorMessage = ex.awsErrorDetails() != null ? ex.awsErrorDetails().errorMessage() : ex.getMessage();
            String errorCode = ex.awsErrorDetails() != null ? ex.awsErrorDetails().errorCode() : "unknown";
            log.error("event=ses_notification_failed statusCode={} errorCode={} message={} subject={}",
                    ex.statusCode(), errorCode, errorMessage, message.subject(), ex);
            throw new NotificationDeliveryException("Failed to send notification via SES", ex);
        } catch (SdkClientException ex) {
            failureCounter.increment();
            log.error("event=ses_notification_failed reason={} subject={}", ex.getMessage(), message.subject(), ex);
            throw new NotificationDeliveryException("Failed to send notification via SES", ex);
        }
    }

    private Destination buildDestination() {
        return Destination.builder()
                .toAddresses(properties.getToAddresses())
                .build();
    }

    private String buildTemplateData(NotificationMessage message) {
        Map<String, Object> templateModel = new LinkedHashMap<>(message.templateModel());
        templateModel.putIfAbsent("subject", message.subject());
        templateModel.putIfAbsent("body", message.body());
        try {
            return objectMapper.writeValueAsString(templateModel);
        } catch (JsonProcessingException e) {
            throw new NotificationDeliveryException("Unable to serialize template model", e);
        }
    }

    private void validateConfiguration() {
        if (properties.getFromAddress() == null || properties.getFromAddress().isBlank()) {
            throw new NotificationDeliveryException("AWS SES from-address is not configured");
        }
        List<String> recipients = properties.getToAddresses();
        if (recipients == null || recipients.isEmpty()) {
            throw new NotificationDeliveryException("AWS SES to-addresses are not configured");
        }
        if (properties.getTemplateName() == null || properties.getTemplateName().isBlank()) {
            throw new NotificationDeliveryException("AWS SES template-name is not configured");
        }
    }

    private void handleSesException(NotificationMessage message, SesV2Exception ex) {
        failureCounter.increment();
        int statusCode = ex.statusCode();
        String errorCode = ex.awsErrorDetails() != null ? ex.awsErrorDetails().errorCode() : null;
        String errorMessage = ex.awsErrorDetails() != null ? ex.awsErrorDetails().errorMessage() : ex.getMessage();
        boolean throttled = statusCode == 429 || "Throttling".equalsIgnoreCase(errorCode);
        if (throttled) {
            throttledCounter.increment();
            log.warn("event=ses_notification_throttled subject={} statusCode={} message={}",
                    message.subject(), statusCode, errorMessage);
            throw new NotificationRateLimitException("SES throttled the request", ex);
        }
        log.error("event=ses_notification_failed statusCode={} errorCode={} message={} subject={}",
                statusCode,
                errorCode != null ? errorCode : "unknown",
                errorMessage,
                message.subject(), ex);
        throw new NotificationDeliveryException("SES rejected the request", ex);
    }
}
