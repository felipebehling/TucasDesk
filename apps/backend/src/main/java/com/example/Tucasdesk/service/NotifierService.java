package com.example.Tucasdesk.service;

import com.example.Tucasdesk.config.AwsSesProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

import java.util.List;

@Service
public class NotifierService {

    private static final Logger log = LoggerFactory.getLogger(NotifierService.class);

    private final ObjectProvider<SesV2Client> sesV2ClientProvider;
    private final AwsSesProperties awsSesProperties;
    private final ObjectMapper objectMapper;

    public NotifierService(ObjectProvider<SesV2Client> sesV2ClientProvider,
                           AwsSesProperties awsSesProperties,
                           ObjectMapper objectMapper) {
        this.sesV2ClientProvider = sesV2ClientProvider;
        this.awsSesProperties = awsSesProperties;
        this.objectMapper = objectMapper;
    }

    @SqsListener("${app.aws.messaging.queue-name}")
    public void receiveMessage(String messageJson) {
        log.info("Received SQS message: {}", messageJson);
        try {
            JsonNode rootNode = objectMapper.readTree(messageJson);
            String eventPayloadString = rootNode.path("Message").asText();
            JsonNode eventPayload = objectMapper.readTree(eventPayloadString);

            String eventType = eventPayload.path("eventType").asText("UNKNOWN");
            int ticketId = eventPayload.path("chamadoId").asInt();

            String subject = "Notification for Ticket #" + ticketId;
            String body = "An event of type '" + eventType + "' occurred for ticket #" + ticketId + ".";

            sendNotificationEmail(subject, body);

        } catch (Exception e) {
            log.error("Failed to process SQS message", e);
        }
    }

    private void sendNotificationEmail(String subject, String body) {
        if (!awsSesProperties.isEnabled()) {
            log.warn("Email sending is skipped because AWS SES is disabled.");
            return;
        }

        SesV2Client sesClient = sesV2ClientProvider.getIfAvailable();
        if (sesClient == null) {
            log.warn("Email sending is skipped because AWS SES client is not available.");
            return;
        }

        List<String> toAddresses = awsSesProperties.getToAddresses();
        if (!StringUtils.hasText(awsSesProperties.getFromAddress()) || CollectionUtils.isEmpty(toAddresses)) {
            log.warn("Email sending failed because 'from' or 'to' addresses are not configured.");
            return;
        }

        Destination destination = Destination.builder()
                .toAddresses(toAddresses)
                .build();

        EmailContent emailContent = EmailContent.builder()
                .simple(Message.builder()
                        .subject(Content.builder().data(subject).build())
                        .body(Body.builder().text(Content.builder().data(body).build()).build())
                        .build())
                .build();

        SendEmailRequest request = SendEmailRequest.builder()
                .fromEmailAddress(awsSesProperties.getFromAddress())
                .destination(destination)
                .content(emailContent)
                .build();

        try {
            sesClient.sendEmail(request);
            log.info("Successfully sent notification email for subject: {}", subject);
        } catch (Exception e) {
            log.error("Failed to send notification email via SES", e);
        }
    }
}
