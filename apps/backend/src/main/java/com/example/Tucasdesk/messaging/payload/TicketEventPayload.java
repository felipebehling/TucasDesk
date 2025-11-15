package com.example.Tucasdesk.messaging.payload;

/**
 * Common contract for ticket domain events published to the messaging layer.
 */
public interface TicketEventPayload {

    /**
     * Event type identifier, used by consumers to route messages.
     *
     * @return the canonical event type.
     */
    String getEventType();

    /**
     * Identifier of the ticket associated with the event.
     *
     * @return the ticket identifier.
     */
    Integer getChamadoId();
}
