package br.com.shared.events;

import java.util.UUID;

public record TicketAssignedEvent(
        UUID ticketId,
        UUID clientId,
        UUID technicianId,
        String email,
        String message
) {
}