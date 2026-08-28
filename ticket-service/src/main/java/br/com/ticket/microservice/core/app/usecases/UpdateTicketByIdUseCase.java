package br.com.ticket.microservice.core.app.usecases;

import br.com.shared.exceptions.exceptionClass.RelatedEntityNotFoundException;
import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.domain.UserDomain;
import br.com.ticket.microservice.core.enums.Status;
import br.com.ticket.microservice.core.gateway.NotificationGateway;
import br.com.ticket.microservice.core.gateway.TicketGateway;
import br.com.ticket.microservice.core.gateway.UserGateway;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.UUID;

public class UpdateTicketByIdUseCase {

    private final TicketGateway ticketGateway;
    private final UserGateway userGateway;
    private final NotificationGateway notificationGateway;

    public UpdateTicketByIdUseCase(
            TicketGateway ticketGateway,
            UserGateway userGateway,
            NotificationGateway notificationGateway
    ) {
        this.ticketGateway = ticketGateway;
        this.userGateway = userGateway;
        this.notificationGateway = notificationGateway;
    }

    public TicketDomain execute(TicketDomain ticket, UUID uuid, String authorization) {

        System.out.println(
                "UpdateTicketByIdUseCase.execute - Ticket UUID: "
                        + uuid
        );

        TicketDomain existingTicket = ticketGateway.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException(
                        "O ticket com o ID " + uuid + " não foi encontrado."
                ));

        Status oldStatus = existingTicket.getStatus();


        UserDomain existingClient = userGateway.findById(
                        existingTicket.getClientDomain().getUuid(), authorization
                )
                .orElseThrow(() -> new RelatedEntityNotFoundException(
                        "Cliente não encontrado."
                ));


        UserDomain existingTechnician = null;

        if(existingTicket.getTechnicianDomain() != null
                && existingTicket.getTechnicianDomain().getUuid() != null){

            existingTechnician = userGateway.findById(
                            existingTicket.getTechnicianDomain().getUuid(), authorization
                    )
                    .orElseThrow(() -> new RelatedEntityNotFoundException(
                            "Técnico não encontrado."
                    ));
        }


        existingTicket.setClientDomain(existingClient);
        existingTicket.setTechnicianDomain(existingTechnician);


        System.out.println(
                "UpdateTicketByIdUseCase.execute - Existing Ticket: "
                        + existingTicket.getUuid()
        );

        System.out.println(
                "UpdateTicketByIdUseCase.execute - Existing Title: "
                        + existingTicket.getTitle()
        );


        existingTicket.setTitle(
                ticket.getTitle() != null
                        ? ticket.getTitle()
                        : existingTicket.getTitle()
        );


        existingTicket.setDescription(
                ticket.getDescription() != null
                        ? ticket.getDescription()
                        : existingTicket.getDescription()
        );


        existingTicket.setCategory(
                ticket.getCategory() != null
                        ? ticket.getCategory()
                        : existingTicket.getCategory()
        );


        existingTicket.setPriority(
                ticket.getPriority() != null
                        ? ticket.getPriority()
                        : existingTicket.getPriority()
        );


        existingTicket.setStatus(
                ticket.getStatus() != null
                        ? ticket.getStatus()
                        : existingTicket.getStatus()
        );


        existingTicket.setUpdatedAt(
                LocalDateTime.now()
        );


        TicketDomain saved =
                ticketGateway.save(existingTicket);


        if(oldStatus != saved.getStatus()){

            notificationGateway.sendTicketStatusChanged(
                    saved,
                    oldStatus
            );
        }


        return saved;
    }
}