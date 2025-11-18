package com.example.Tucasdesk.service;

import com.example.Tucasdesk.exception.ResourceNotFoundException;
import com.example.Tucasdesk.messaging.ChamadoMessagingService;
import com.example.Tucasdesk.model.Chamado;
import com.example.Tucasdesk.model.Interacao;
import com.example.Tucasdesk.repository.ChamadoRepository;
import com.example.Tucasdesk.repository.InteracaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InteracaoServiceTest {

    @Mock
    private InteracaoRepository interacaoRepository;

    @Mock
    private ChamadoRepository chamadoRepository;

    @Mock
    private ChamadoMessagingService chamadoMessagingService;

    @InjectMocks
    private InteracaoService interacaoService;

    @Test
    void createInteraction_shouldSaveAndPublishEvent() {
        // Arrange
        Integer ticketId = 1;
        Chamado mockTicket = new Chamado();
        mockTicket.setIdChamado(ticketId);

        Interacao newInteraction = new Interacao();
        newInteraction.setMensagem("Test message");

        when(chamadoRepository.findById(ticketId)).thenReturn(Optional.of(mockTicket));
        when(interacaoRepository.save(any(Interacao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Interacao result = interacaoService.createInteraction(ticketId, newInteraction);

        // Assert
        assertNotNull(result);
        assertEquals(mockTicket, result.getChamado());
        verify(interacaoRepository, times(1)).save(newInteraction);
        verify(chamadoMessagingService, times(1)).publishChamadoInteracaoAddedEvent(mockTicket, result);
    }

    @Test
    void createInteraction_shouldThrowResourceNotFoundException_whenTicketNotFound() {
        // Arrange
        Integer ticketId = 99;
        Interacao newInteraction = new Interacao();
        newInteraction.setMensagem("Test message");

        when(chamadoRepository.findById(ticketId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            interacaoService.createInteraction(ticketId, newInteraction);
        });

        verify(interacaoRepository, never()).save(any());
        verify(chamadoMessagingService, never()).publishChamadoInteracaoAddedEvent(any(), any());
    }
}
