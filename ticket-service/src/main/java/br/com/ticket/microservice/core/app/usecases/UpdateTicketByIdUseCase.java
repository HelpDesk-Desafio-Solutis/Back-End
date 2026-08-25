package br.com.ticket.microservice.core.app.usecases;

import br.com.shared.exceptions.exceptionClass.RelatedEntityNotFoundException;
import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.domain.UserDomain;
import br.com.ticket.microservice.core.gateway.TicketGateway;
import br.com.ticket.microservice.core.gateway.UserGateway;
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

        UserDomain client = userGateway.findById(ticket.getClientDomain().getUuid())
                .orElseThrow(() -> new RelatedEntityNotFoundException(
                        "O cliente com o ID " + ticket.getClientDomain().getUuid() + " não foi encontrado ou está inativo."
                ));

        UserDomain technician = userGateway.findById(ticket.getTechnicianDomain().getUuid())
                .orElseThrow(() -> new RelatedEntityNotFoundException(
                        "O técnico com o ID " + ticket.getTechnicianDomain().getUuid() + " não foi encontrado ou está inativo."
                ));

        ticket.setClientDomain(client);
        ticket.setTechnicianDomain(technician);

        ticket.setUuid(uuid);
        ticket.setCreatedAt(ticketGateway.findById(uuid).get().getCreatedAt());
        ticket.setUpdatedAt(LocalDateTime.now());
        return ticketGateway.save(ticket);
    }

}
