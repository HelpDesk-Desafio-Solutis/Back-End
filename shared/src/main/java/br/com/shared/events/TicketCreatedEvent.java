package br.com.shared.events;

import java.util.UUID;

public record TicketCreatedEvent(
        UUID ticketId,
        UUID userId,
        String email,
        String message
) {
}
