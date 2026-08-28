package br.com.ticket.microservice.core.app.usecases;

import br.com.shared.exceptions.exceptionClass.RelatedEntityNotFoundException;
import br.com.ticket.microservice.core.app.dto.user.UserResponseDto;
import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.domain.UserDomain;
import br.com.ticket.microservice.core.gateway.NotificationGateway;
import br.com.ticket.microservice.core.gateway.TicketGateway;
import br.com.ticket.microservice.core.gateway.UserGateway;

import java.time.LocalDateTime;

public class CreateTicketAdminUseCase {

    private final TicketGateway ticketGateway;
    private final UserGateway userGateway;
    private final NotificationGateway notificationGateway;


    public CreateTicketAdminUseCase(
            TicketGateway ticketGateway,
            UserGateway userGateway,
            NotificationGateway notificationGateway
    ) {
        this.ticketGateway = ticketGateway;
        this.userGateway = userGateway;
        this.notificationGateway = notificationGateway;
    }


    public TicketDomain execute(TicketDomain ticket, String authorization) {


        UserDomain client =
                userGateway.findById(
                        ticket.getClientDomain().getUuid(),
                        authorization
                ).orElseThrow(() ->
                        new RelatedEntityNotFoundException(
                                "Cliente não encontrado."
                        )
                );


        UserDomain technician =
                userGateway.findById(
                                ticket.getTechnicianDomain().getUuid(),
                                authorization
                        )
                        .orElseThrow(() ->
                                new RelatedEntityNotFoundException(
                                        "Técnico não encontrado."
                                )
                        );


        ticket.setClientDomain(client);
        ticket.setTechnicianDomain(technician);


        ticket.setUuid(null);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());


        TicketDomain saved =
                ticketGateway.save(ticket);


        notificationGateway.sendTicketCreated(saved);
        notificationGateway.sendTicketAssigned(saved);


        return saved;
    }
}
