package br.com.ticket.microservice.core.app.usecases;

import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.domain.UserDomain;
import br.com.ticket.microservice.core.enums.Status;
import br.com.ticket.microservice.core.gateway.TicketGateway;
import br.com.ticket.microservice.core.gateway.UserGateway;

import java.util.List;

public class GetAllTicketUseCase {

    private final TicketGateway gateway;
    private final UserGateway userGateway;

    public GetAllTicketUseCase(TicketGateway gateway, UserGateway userGateway) {
        this.gateway = gateway;
        this.userGateway = userGateway;
    }

    public List<TicketDomain> execute(Status status, String authorization) {
        List<TicketDomain> tickets = status != null
                ? gateway.findAll(status)
                : gateway.findAll();

        tickets.forEach(ticket -> enrich(ticket, authorization));

        return tickets;
    }

    private void enrich(TicketDomain ticket, String authorization) {
        if (ticket.getClientDomain() != null &&
                ticket.getClientDomain().getUuid() != null) {

            userGateway.findById(
                    ticket.getClientDomain().getUuid(),
                    authorization
            ).ifPresent(ticket::setClientDomain);
        }

        if (ticket.getTechnicianDomain() != null &&
                ticket.getTechnicianDomain().getUuid() != null) {

            userGateway.findById(
                    ticket.getTechnicianDomain().getUuid(),
                    authorization
            ).ifPresent(ticket::setTechnicianDomain);
        }
    }
}