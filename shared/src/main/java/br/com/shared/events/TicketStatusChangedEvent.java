package br.com.shared.events;

import java.util.UUID;

public record TicketStatusChangedEvent(
        UUID ticketId,
        UUID clientId,
        UUID technicianId,
        String email,
        String oldStatus,
        String newStatus,
        String message
) {
}
