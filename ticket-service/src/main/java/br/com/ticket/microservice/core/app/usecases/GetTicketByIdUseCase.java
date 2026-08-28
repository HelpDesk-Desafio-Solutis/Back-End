package br.com.ticket.microservice.core.app.usecases;

import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.domain.UserDomain;
import br.com.ticket.microservice.core.gateway.TicketGateway;
import br.com.ticket.microservice.core.gateway.UserGateway;

import java.util.UUID;

public class GetTicketByIdUseCase {

    private final TicketGateway gateway;
    private final UserGateway userGateway;

    public GetTicketByIdUseCase(
            TicketGateway gateway,
            UserGateway userGateway
    ) {
        this.gateway = gateway;
        this.userGateway = userGateway;
    }

    public TicketDomain execute(UUID uuid, String authorization) {
        TicketDomain ticket = gateway.findById(uuid)
                .orElseThrow(() -> new RuntimeException(
                        "Ticket não encontrado."
                ));

        UserDomain client = userGateway.findById(
                ticket.getClientDomain().getUuid(), authorization
        ).orElseThrow(() -> new RuntimeException(
                "Cliente não encontrado."
        ));

        UserDomain technician = null;

        if(ticket.getTechnicianDomain() != null){
            technician = userGateway.findById(
                    ticket.getTechnicianDomain().getUuid(), authorization
            ).orElseThrow(() -> new RuntimeException(
                    "Técnico não encontrado."
            ));
        }

        ticket.setClientDomain(client);
        ticket.setTechnicianDomain(technician);

        return ticket;
    }

}
