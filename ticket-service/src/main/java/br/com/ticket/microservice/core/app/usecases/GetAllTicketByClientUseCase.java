package br.com.ticket.microservice.core.app.usecases;

import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.gateway.TicketGateway;

import java.util.List;
import java.util.UUID;

public class GetAllTicketByClientUseCase {

    private final TicketGateway gateway;

    public GetAllTicketByClientUseCase(TicketGateway gateway) {
        this.gateway = gateway;
    }

    public List<TicketDomain> execute(UUID uuid) {
        return gateway.findAllByClientId(uuid);
    }

}
