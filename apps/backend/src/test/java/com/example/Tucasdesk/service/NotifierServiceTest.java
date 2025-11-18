package com.example.Tucasdesk.service;

import com.example.Tucasdesk.config.AwsSesProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.SendEmailRequest;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotifierServiceTest {

    @Mock
    private ObjectProvider<SesV2Client> sesV2ClientProvider;

    @Mock
    private SesV2Client sesV2Client;

    @Mock
    private AwsSesProperties awsSesProperties;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private NotifierService notifierService;

    @BeforeEach
    void setUp() {
        // Inject the ObjectMapper manually as it's not a mock
        notifierService = new NotifierService(sesV2ClientProvider, awsSesProperties, objectMapper);
    }

    @Test
    void receiveMessage_shouldParseAndSendEmail() {
        // Arrange
        String eventPayload = "{\"eventType\":\"TICKET_CREATED\",\"chamadoId\":123}";
        String snsMessage = String.format("{\"Message\":\"%s\"}", eventPayload.replace("\"", "\\\""));

        when(awsSesProperties.isEnabled()).thenReturn(true);
        when(awsSesProperties.getFromAddress()).thenReturn("sender@example.com");
        when(awsSesProperties.getToAddresses()).thenReturn(List.of("recipient@example.com"));
        when(sesV2ClientProvider.getIfAvailable()).thenReturn(sesV2Client);

        // Act
        notifierService.receiveMessage(snsMessage);

        // Assert
        verify(sesV2Client, times(1)).sendEmail(any(SendEmailRequest.class));
    }

    @Test
    void receiveMessage_shouldDoNothing_whenSesIsDisabled() {
        // Arrange
        String snsMessage = "{\"Message\":\"{}\"}";
        when(awsSesProperties.isEnabled()).thenReturn(false);

        // Act
        notifierService.receiveMessage(snsMessage);

        // Assert
        verify(sesV2ClientProvider, never()).getIfAvailable();
        verify(sesV2Client, never()).sendEmail(any(SendEmailRequest.class));
    }

    @Test
    void receiveMessage_shouldLogError_onInvalidJson() {
        // Arrange
        String invalidJson = "this is not json";

        // Act
        notifierService.receiveMessage(invalidJson);

        // Assert
        verify(sesV2Client, never()).sendEmail(any(SendEmailRequest.class));
    }
}
