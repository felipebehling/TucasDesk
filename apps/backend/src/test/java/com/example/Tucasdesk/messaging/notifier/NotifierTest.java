package com.example.Tucasdesk.messaging.notifier;

import com.example.Tucasdesk.messaging.ChamadoEventPayload;
import com.example.Tucasdesk.messaging.ChamadoMessagingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import org.assertj.core.api.InstanceOfAssertFactories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotifierTest {

    @Mock
    private NotificationSender sender;

    private Notifier notifier;

    @BeforeEach
    void setUp() {
        notifier = new Notifier(sender);
    }

    @Test
    void shouldComposeNotificationForCreatedTicket() {
        ChamadoEventPayload payload = new ChamadoEventPayload(
                ChamadoMessagingService.EVENT_CHAMADO_CREATED,
                101,
                "Novo computador",
                "Instalação de estação de trabalho",
                10,
                20,
                null,
                null,
                1,
                LocalDateTime.of(2024, 10, 5, 14, 30),
                null,
                null
        );

        notifier.notify(payload);

        ArgumentCaptor<NotificationMessage> messageCaptor = ArgumentCaptor.forClass(NotificationMessage.class);
        verify(sender).send(messageCaptor.capture());
        NotificationMessage message = messageCaptor.getValue();
        assertThat(message.subject()).isEqualTo("Chamado #101 criado: Novo computador");
        assertThat(message.body())
                .contains("Evento: CHAMADO_CREATED")
                .contains("Chamado: #101")
                .contains("Título: Novo computador")
                .contains("Descrição: Instalação de estação de trabalho")
                .contains("Aberto em: 05/10/2024 14:30");
        assertThat(message.templateModel())
                .containsEntry("subject", "Chamado #101 criado: Novo computador")
                .containsEntry("eventType", ChamadoMessagingService.EVENT_CHAMADO_CREATED)
                .containsEntry("ticketId", 101)
                .containsEntry("titulo", "Novo computador")
                .containsEntry("descricao", "Instalação de estação de trabalho")
                .containsEntry("dataAbertura", "05/10/2024 14:30");
    }

    @Test
    void shouldIncludeInteractionInformationInNotificationBody() {
        ChamadoEventPayload payload = new ChamadoEventPayload(
                ChamadoMessagingService.EVENT_CHAMADO_INTERACAO_ADDED,
                55,
                "Problema no e-mail",
                "Cliente não consegue enviar mensagens",
                10,
                22,
                null,
                null,
                3,
                LocalDateTime.of(2024, 9, 15, 9, 0),
                null,
                new ChamadoEventPayload.InteracaoPayload(
                        900,
                        10,
                        "Enviei logs para análise",
                        LocalDateTime.of(2024, 9, 15, 10, 15),
                        "https://s3.amazonaws.com/anexos/logs.txt"
                )
        );

        NotificationMessage message = notifier.buildMessage(payload);

        assertThat(message.subject()).isEqualTo("Nova interação no chamado #55");
        assertThat(message.body())
                .contains("Interação #900")
                .contains("Usuário: 10")
                .contains("Mensagem: Enviei logs para análise")
                .contains("Anexo: https://s3.amazonaws.com/anexos/logs.txt")
                .contains("Data: 15/09/2024 10:15");
        assertThat(message.templateModel())
                .containsEntry("ticketId", 55)
                .containsEntry("titulo", "Problema no e-mail")
                .containsKey("interacao");
        assertThat(message.templateModel())
                .extractingByKey("interacao")
                .asInstanceOf(InstanceOfAssertFactories.MAP)
                .containsEntry("interacaoId", 900)
                .containsEntry("usuarioId", 10)
                .containsEntry("mensagem", "Enviei logs para análise")
                .containsEntry("anexoUrl", "https://s3.amazonaws.com/anexos/logs.txt")
                .containsEntry("dataInteracao", "15/09/2024 10:15");
    }
}
