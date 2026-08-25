package desk.help.core.app.usecases.ticket;

import desk.help.core.app.usecases.exceptions.exceptionClass.RelatedEntityNotFoundException;
import desk.help.core.domains.TicketDomain;
import desk.help.core.gateway.TicketGateway;
import desk.help.core.gateway.UserGateway;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.UUID;

public class UpdateTicketByIdUseCase {

    private final TicketGateway ticketGateway;
    private final UserGateway userGateway;

    public UpdateTicketByIdUseCase(TicketGateway ticketGateway, UserGateway userGateway) {
        this.ticketGateway = ticketGateway;
        this.userGateway = userGateway;
    }

    public TicketDomain execute(TicketDomain ticket, UUID uuid) {
        if (!ticketGateway.existsById(uuid)) {
            throw new EntityNotFoundException(
                    "O ticket com o ID " + uuid + " não foi encontrado."
            );
        }

        if (!userGateway.existsByIdAndActiveTrue(ticket.getClientDomain().getUuid())) {
            throw new RelatedEntityNotFoundException(
                    "O cliente com o ID " + ticket.getClientDomain().getUuid() + " não foi encontrado ou está inativo."
            );
        }

        if (!userGateway.existsByIdAndActiveTrue(ticket.getTechnicianDomain().getUuid())) {
            throw new RelatedEntityNotFoundException(
                    "O técnico com o ID " + ticket.getTechnicianDomain().getUuid() + " não foi encontrado ou está inativo."
            );
        }

        ticket.setUuid(uuid);
        ticket.setCreatedAt(ticketGateway.findById(uuid).get().getCreatedAt());
        ticket.setUpdatedAt(LocalDateTime.now());
        return ticketGateway.save(ticket);
    }

}
