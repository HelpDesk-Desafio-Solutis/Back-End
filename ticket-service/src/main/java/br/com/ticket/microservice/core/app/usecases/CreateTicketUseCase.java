package br.com.ticket.microservice.core.app.usecases;

import br.com.shared.exceptions.exceptionClass.RelatedEntityNotFoundException;
import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.domain.UserDomain;
import br.com.ticket.microservice.core.gateway.NotificationGateway;
import br.com.ticket.microservice.core.gateway.TicketGateway;
import br.com.ticket.microservice.core.gateway.UserGateway;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateTicketUseCase {

    private final TicketGateway ticketGateway;
    private final UserGateway userGateway;
    private final NotificationGateway notificationGateway;

    public CreateTicketUseCase(TicketGateway ticketGateway, UserGateway userGateway, NotificationGateway notificationGateway) {
        this.ticketGateway = ticketGateway;
        this.userGateway = userGateway;
        this.notificationGateway = notificationGateway;
    }

    public TicketDomain execute(TicketDomain ticket, String authorization, UUID userUuid) throws Exception {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RelatedEntityNotFoundException(
                    "Usuário autenticado não encontrado."
            );
        }

        if (userUuid == null) {
            throw new RelatedEntityNotFoundException(
                    "UUID do usuário autenticado inválido: " + userUuid
            );
        }

        UserDomain client =
                userGateway.findById(
                        userUuid,
                        authorization
                ).orElseThrow(() ->
                        new RelatedEntityNotFoundException(
                                "Cliente com o ID "
                                        + userUuid
                                        + " não encontrado."
                        )
                );

        System.out.println("====================================");
        System.out.println("CLIENTE AUTENTICADO:");
        System.out.println("UUID: " + client.getUuid());
        System.out.println("NOME: " + client.getName());
        System.out.println("EMAIL: " + client.getEmail());
        System.out.println("ROLE: " + client.getRole());
        System.out.println("====================================");

        ticket.setClientDomain(client);

        if (ticket.getTechnicianDomain() != null &&
                ticket.getTechnicianDomain().getUuid() != null) {

            UserDomain technician =
                    userGateway.findById(
                            ticket.getTechnicianDomain().getUuid(),
                            authorization
                    ).orElseThrow(() ->
                            new RelatedEntityNotFoundException(
                                    "Técnico com o ID "
                                            + ticket.getTechnicianDomain().getUuid()
                                            + " não encontrado."
                            )
                    );

            System.out.println("====================================");
            System.out.println("TÉCNICO APÓS BUSCA NO USER-SERVICE:");
            System.out.println("UUID: " + technician.getUuid());
            System.out.println("NOME: " + technician.getName());
            System.out.println("EMAIL: " + technician.getEmail());
            System.out.println("ROLE: " + technician.getRole());
            System.out.println("====================================");

            ticket.setTechnicianDomain(technician);
        }

        LocalDateTime now = LocalDateTime.now();

        ticket.setUuid(null);
        ticket.setCreatedAt(now);
        ticket.setUpdatedAt(now);

        TicketDomain savedTicket = ticketGateway.save(ticket);

        savedTicket.setClientDomain(ticket.getClientDomain());
        savedTicket.setTechnicianDomain(ticket.getTechnicianDomain());

        notificationGateway.sendTicketCreated(savedTicket);

        if (savedTicket.getTechnicianDomain() != null) {
            notificationGateway.sendTicketAssigned(savedTicket);
        }

        return savedTicket;
    }
}