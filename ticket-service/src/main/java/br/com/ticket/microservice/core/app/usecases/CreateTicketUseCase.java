package br.com.ticket.microservice.core.app.usecases;

import br.com.shared.events.TicketCreatedEvent;
import br.com.shared.exceptions.exceptionClass.RelatedEntityNotFoundException;
import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.domain.UserDomain;
import br.com.ticket.microservice.core.gateway.NotificationGateway;
import br.com.ticket.microservice.core.gateway.TicketGateway;
import br.com.ticket.microservice.core.gateway.UserGateway;

import java.time.LocalDateTime;

public class CreateTicketUseCase {

    private final TicketGateway ticketGateway;
    private final UserGateway userGateway;
    private final NotificationGateway notificationGateway;

    public CreateTicketUseCase(TicketGateway ticketGateway, UserGateway userGateway, NotificationGateway notificationGateway) {
        this.ticketGateway = ticketGateway;
        this.userGateway = userGateway;
        this.notificationGateway = notificationGateway;
    }

    public TicketDomain execute(TicketDomain ticket) throws Exception {
        if (ticket.getClientDomain() != null && ticket.getClientDomain().getUuid() != null) {
            UserDomain client = userGateway.findById(ticket.getClientDomain().getUuid())
                    .orElseThrow(() -> new RelatedEntityNotFoundException("Cliente com o ID " + ticket.getClientDomain().getUuid() + " não encontrado."));

            System.out.println("CLIENTE APÓS BUSCA NO USER-SERVICE:");
            System.out.println("UUID: " + client.getUuid());
            System.out.println("EMAIL: " + client.getEmail());

            ticket.setClientDomain(client);
        }

        if (ticket.getTechnicianDomain() != null && ticket.getTechnicianDomain().getUuid() != null) {
            UserDomain technician = userGateway.findById(ticket.getTechnicianDomain().getUuid())
                    .orElseThrow(() -> new RelatedEntityNotFoundException("Técnico com o ID " + ticket.getTechnicianDomain().getUuid() + " não encontrado."));

            System.out.println("TÉCNICO APÓS BUSCA NO USER-SERVICE:");
            System.out.println("UUID: " + technician.getUuid());
            System.out.println("EMAIL: " + technician.getEmail());

            ticket.setTechnicianDomain(technician);
        }

        ticket.setUuid(null);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        TicketDomain savedTicket = ticketGateway.save(ticket);

        savedTicket.setClientDomain(ticket.getClientDomain());
        savedTicket.setTechnicianDomain(ticket.getTechnicianDomain());

        notificationGateway.sendTicketCreated(savedTicket);
        notificationGateway.sendTicketAssigned(savedTicket);

        return savedTicket;
    }
}
