package com.example.Tucasdesk.messaging;

import com.example.Tucasdesk.config.AwsMessagingProperties;
import com.example.Tucasdesk.messaging.payload.TicketClosedEventPayload;
import com.example.Tucasdesk.messaging.payload.TicketCreatedEventPayload;
import com.example.Tucasdesk.model.Chamado;
import com.example.Tucasdesk.model.Status;
import com.example.Tucasdesk.model.Usuario;
import io.awspring.cloud.sns.core.SnsTemplate;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ChamadoMessagingServiceIT.TestConfig.class)
@TestPropertySource(properties = {
        "app.aws.messaging.topics.ticket-created-arn=arn:aws:sns:sa-east-1:123456789012:ticket-created",
        "app.aws.messaging.topics.ticket-closed-arn=arn:aws:sns:sa-east-1:123456789012:ticket-closed",
        "app.aws.messaging.queue-name=ticket-events-queue"
})
class ChamadoMessagingServiceIT {

    private final ChamadoMessagingService messagingService;
    private final SnsTemplate snsTemplate;
    private final SqsTemplate sqsTemplate;

    ChamadoMessagingServiceIT(ChamadoMessagingService messagingService,
                              SnsTemplate snsTemplate,
                              SqsTemplate sqsTemplate) {
        this.messagingService = messagingService;
        this.snsTemplate = snsTemplate;
        this.sqsTemplate = sqsTemplate;
    }

    @BeforeEach
    void resetMocks() {
        Mockito.reset(snsTemplate, sqsTemplate);
    }

    @Test
    void shouldPublishTicketCreatedPayloadToDedicatedTopicAndQueue() {
        Chamado chamado = criarChamadoAberto();

        messagingService.publishChamadoCreatedEvent(chamado);

        ArgumentCaptor<TicketCreatedEventPayload> snsPayloadCaptor = ArgumentCaptor.forClass(TicketCreatedEventPayload.class);
        verify(snsTemplate).convertAndSend(eq("arn:aws:sns:sa-east-1:123456789012:ticket-created"), snsPayloadCaptor.capture());
        TicketCreatedEventPayload snsPayload = snsPayloadCaptor.getValue();
        Assertions.assertThat(snsPayload.eventType()).isEqualTo(TicketCreatedEventPayload.EVENT_TYPE);
        Assertions.assertThat(snsPayload.chamadoId()).isEqualTo(chamado.getIdChamado());
        Assertions.assertThat(snsPayload.solicitanteId()).isEqualTo(chamado.getUsuario().getIdUsuario());

        ArgumentCaptor<ChamadoEventPayload> sqsPayloadCaptor = ArgumentCaptor.forClass(ChamadoEventPayload.class);
        verify(sqsTemplate).send(eq("ticket-events-queue"), sqsPayloadCaptor.capture());
        ChamadoEventPayload sqsPayload = sqsPayloadCaptor.getValue();
        Assertions.assertThat(sqsPayload.eventType()).isEqualTo(ChamadoMessagingService.EVENT_CHAMADO_CREATED);
        Assertions.assertThat(sqsPayload.chamadoId()).isEqualTo(chamado.getIdChamado());
    }

    @Test
    void shouldPublishTicketClosedPayloadToDedicatedTopic() {
        Chamado chamado = criarChamadoAberto();
        Status fechado = new Status();
        fechado.setIdStatus(3);
        fechado.setNome("Fechado");
        chamado.setStatus(fechado);
        chamado.setDataFechamento(LocalDateTime.now());

        messagingService.publishChamadoClosedEvent(chamado);

        ArgumentCaptor<TicketClosedEventPayload> snsPayloadCaptor = ArgumentCaptor.forClass(TicketClosedEventPayload.class);
        verify(snsTemplate).convertAndSend(eq("arn:aws:sns:sa-east-1:123456789012:ticket-closed"), snsPayloadCaptor.capture());
        TicketClosedEventPayload payload = snsPayloadCaptor.getValue();
        Assertions.assertThat(payload.eventType()).isEqualTo(TicketClosedEventPayload.EVENT_TYPE);
        Assertions.assertThat(payload.chamadoId()).isEqualTo(chamado.getIdChamado());

        verifyNoInteractions(sqsTemplate);
    }

    private Chamado criarChamadoAberto() {
        Chamado chamado = new Chamado();
        chamado.setIdChamado(42);
        chamado.setTitulo("Computador não liga");
        chamado.setDescricao("Equipamento parou de funcionar depois de atualização.");
        Usuario solicitante = new Usuario();
        solicitante.setIdUsuario(10);
        chamado.setUsuario(solicitante);
        Usuario tecnico = new Usuario();
        tecnico.setIdUsuario(20);
        chamado.setTecnico(tecnico);
        chamado.setDataAbertura(LocalDateTime.now().minusHours(1));
        Status status = new Status();
        status.setIdStatus(1);
        status.setNome("Aberto");
        chamado.setStatus(status);
        return chamado;
    }

    @Configuration
    @EnableConfigurationProperties(AwsMessagingProperties.class)
    static class TestConfig {

        @Bean
        SnsTemplate snsTemplate() {
            return Mockito.mock(SnsTemplate.class);
        }

        @Bean
        SqsTemplate sqsTemplate() {
            return Mockito.mock(SqsTemplate.class);
        }

        @Bean
        ChamadoMessagingService chamadoMessagingService(SnsTemplate snsTemplate,
                                                        SqsTemplate sqsTemplate,
                                                        AwsMessagingProperties properties) {
            return new ChamadoMessagingService(snsTemplate, sqsTemplate, properties);
        }
    }
}
