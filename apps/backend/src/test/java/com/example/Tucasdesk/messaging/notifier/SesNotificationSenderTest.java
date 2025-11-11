package com.example.Tucasdesk.messaging.notifier;

import com.example.Tucasdesk.config.AwsSesProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.awscore.exception.AwsErrorDetails;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.SendEmailRequest;
import software.amazon.awssdk.services.sesv2.model.SendEmailResponse;
import software.amazon.awssdk.services.sesv2.model.SesV2Exception;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SesNotificationSenderTest {

    @Mock
    private SesV2Client sesClient;

    private SimpleMeterRegistry meterRegistry;
    private AwsSesProperties properties;
    private SesNotificationSender sender;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        properties = new AwsSesProperties();
        properties.setEnabled(true);
        properties.setRegion("sa-east-1");
        properties.setFromAddress("notificacoes@example.com");
        properties.setToAddresses(List.of("cliente@example.com", "suporte@example.com"));
        properties.setTemplateName("tucasdesk-ticket-update");
        objectMapper = new ObjectMapper();
        sender = new SesNotificationSender(sesClient, properties, objectMapper, meterRegistry);
    }

    @Test
    void shouldSendTemplatedEmailWithSubjectAndBody() throws Exception {
        when(sesClient.sendEmail(any(SendEmailRequest.class)))
                .thenReturn(SendEmailResponse.builder().messageId("123").build());

        Map<String, Object> templateModel = Map.of("eventType", "CHAMADO_CREATED", "ticketId", 42);
        NotificationMessage message = new NotificationMessage("Chamado criado", "Detalhes do chamado", templateModel);

        sender.send(message);

        ArgumentCaptor<SendEmailRequest> requestCaptor = ArgumentCaptor.forClass(SendEmailRequest.class);
        verify(sesClient).sendEmail(requestCaptor.capture());

        SendEmailRequest request = requestCaptor.getValue();
        assertThat(request.fromEmailAddress()).isEqualTo("notificacoes@example.com");
        assertThat(request.destination().toAddresses())
                .containsExactly("cliente@example.com", "suporte@example.com");
        assertThat(request.content().template().templateName()).isEqualTo("tucasdesk-ticket-update");

        JsonNode templateData = objectMapper.readTree(request.content().template().templateData());
        assertThat(templateData.get("subject").asText()).isEqualTo("Chamado criado");
        assertThat(templateData.get("body").asText()).contains("Detalhes do chamado");
        assertThat(templateData.get("eventType").asText()).isEqualTo("CHAMADO_CREATED");
        assertThat(templateData.get("ticketId").asInt()).isEqualTo(42);

        double successCount = meterRegistry.get("notifier.ses.deliveries").tag("outcome", "success").counter().count();
        assertThat(successCount).isEqualTo(1.0);
    }

    @Test
    void shouldThrowRateLimitExceptionWhenThrottled() {
        SesV2Exception throttled = (SesV2Exception) SesV2Exception.builder()
                .statusCode(429)
                .awsErrorDetails(AwsErrorDetails.builder()
                        .errorCode("Throttling")
                        .errorMessage("Daily quota exceeded")
                        .serviceName("ses")
                        .build())
                .build();
        when(sesClient.sendEmail(any(SendEmailRequest.class))).thenThrow(throttled);

        NotificationMessage message = new NotificationMessage("Chamado criado", "Detalhes", Map.of());

        assertThatThrownBy(() -> sender.send(message))
                .isInstanceOf(NotificationRateLimitException.class);

        double failureCount = meterRegistry.get("notifier.ses.deliveries").tag("outcome", "failure").counter().count();
        double throttledCount = meterRegistry.get("notifier.ses.throttled").counter().count();
        assertThat(failureCount).isEqualTo(1.0);
        assertThat(throttledCount).isEqualTo(1.0);
    }

    @Test
    void shouldThrowDeliveryExceptionForOtherSesErrors() {
        SesV2Exception internalError = (SesV2Exception) SesV2Exception.builder()
                .statusCode(500)
                .awsErrorDetails(AwsErrorDetails.builder()
                        .errorCode("InternalFailure")
                        .errorMessage("Internal error")
                        .serviceName("ses")
                        .build())
                .build();
        when(sesClient.sendEmail(any(SendEmailRequest.class))).thenThrow(internalError);

        NotificationMessage message = new NotificationMessage("Chamado criado", "Detalhes", Map.of());

        assertThatThrownBy(() -> sender.send(message))
                .isInstanceOf(NotificationDeliveryException.class)
                .isNotInstanceOf(NotificationRateLimitException.class);

        double failureCount = meterRegistry.get("notifier.ses.deliveries").tag("outcome", "failure").counter().count();
        assertThat(failureCount).isEqualTo(1.0);
    }
}
