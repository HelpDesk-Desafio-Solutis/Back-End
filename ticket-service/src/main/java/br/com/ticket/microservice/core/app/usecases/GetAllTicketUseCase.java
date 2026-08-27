package br.com.ticket.microservice.core.app.usecases;

import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.enums.Status;
import br.com.ticket.microservice.core.gateway.TicketGateway;

import java.util.List;

public class GetAllTicketUseCase {

    private final TicketGateway gateway;

    public GetAllTicketUseCase(TicketGateway gateway) {
        this.gateway = gateway;
    }

    public List<TicketDomain> execute() {
        return gateway.findAll();
    }

    public List<TicketDomain> execute(Status status) {
        return gateway.findAll(status);
    }

}
