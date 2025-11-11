package com.example.Tucasdesk.messaging;

import com.example.Tucasdesk.messaging.notifier.Notifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ChamadoEventListenerTest {

    @Mock
    private Notifier notifier;

    private ChamadoEventListener listener;

    @BeforeEach
    void setUp() {
        listener = new ChamadoEventListener(notifier);
    }

    @Test
    void shouldDelegateNotificationToNotifier() {
        ChamadoEventPayload payload = new ChamadoEventPayload(
                ChamadoMessagingService.EVENT_CHAMADO_CREATED,
                1,
                "Título",
                "Descrição",
                10,
                20,
                null,
                null,
                1,
                null,
                null,
                null
        );

        listener.onChamadoEvent(payload, "3");

        verify(notifier).notify(payload);
    }

    @Test
    void shouldIgnoreNullPayload() {
        listener.onChamadoEvent(null, null);

        verifyNoInteractions(notifier);
    }
}
