package br.com.ticket.microservice.core.app.usecases;

import br.com.shared.exceptions.exceptionClass.InactiveEntityException;
import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.enums.Status;
import br.com.ticket.microservice.core.gateway.TicketGateway;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.UUID;

public class DeactivateTicketByIdUseCase {

    private final TicketGateway gateway;

    public DeactivateTicketByIdUseCase(TicketGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(UUID uuid) {
        if (!gateway.existsById(uuid)) {
            throw new EntityNotFoundException(
                    "O ticket com o ID " + uuid + " não foi encontrado."
            );
        }

        if (gateway.existsByIdAndStatus(uuid, Status.CLOSED)) {
            throw new InactiveEntityException(
                    "O ticket com o ID " + uuid + " já está inativo."
            );
        }

        TicketDomain ticket = gateway.findById(uuid).get();
        ticket.setStatus(Status.CLOSED);
        ticket.setUpdatedAt(LocalDateTime.now());
        gateway.save(ticket);
    }

}
