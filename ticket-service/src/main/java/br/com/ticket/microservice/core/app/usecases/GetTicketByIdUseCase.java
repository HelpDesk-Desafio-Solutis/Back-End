package br.com.ticket.microservice.core.app.usecases;

import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.gateway.TicketGateway;

import java.util.UUID;

public class GetTicketByIdUseCase {

    private final TicketGateway gateway;

    public GetTicketByIdUseCase(TicketGateway gateway) {
        this.gateway = gateway;
    }

    public TicketDomain execute(UUID uuid) {
        return gateway.findById(uuid).get();
    }

}
