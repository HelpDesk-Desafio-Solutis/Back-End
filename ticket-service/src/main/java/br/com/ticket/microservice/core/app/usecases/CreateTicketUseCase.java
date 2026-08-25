package br.com.ticket.microservice.core.app.usecases;

import br.com.shared.exceptions.exceptionClass.RelatedEntityNotFoundException;
import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.gateway.TicketGateway;
import br.com.user.microservice.core.domain.UserDomain;
import br.com.user.microservice.core.gateway.UserGateway;

import java.time.LocalDateTime;

public class CreateTicketUseCase {

    private final TicketGateway ticketGateway;
    private final UserGateway userGateway;

    public CreateTicketUseCase(TicketGateway ticketGateway, UserGateway userGateway) {
        this.ticketGateway = ticketGateway;
        this.userGateway = userGateway;
    }

    public TicketDomain execute(TicketDomain ticket) throws Exception {
        if (ticket.getClientDomain() != null && ticket.getClientDomain().getUuid() != null) {
            UserDomain client = userGateway.findById(ticket.getClientDomain().getUuid())
                    .orElseThrow(() -> new RelatedEntityNotFoundException("Cliente com o ID " + ticket.getClientDomain().getUuid() + " não encontrado."));
            ticket.setClientDomain(client);
        }

        if (ticket.getTechnicianDomain() != null && ticket.getTechnicianDomain().getUuid() != null) {
            UserDomain technician = userGateway.findById(ticket.getTechnicianDomain().getUuid())
                    .orElseThrow(() -> new RelatedEntityNotFoundException("Técnico com o ID " + ticket.getTechnicianDomain().getUuid() + " não encontrado."));
            ticket.setTechnicianDomain(technician);
        }

        ticket.setUuid(null);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        TicketDomain savedTicket = ticketGateway.save(ticket);

        return savedTicket;
    }
}
