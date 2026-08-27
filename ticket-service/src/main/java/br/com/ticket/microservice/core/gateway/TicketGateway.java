package br.com.ticket.microservice.core.gateway;

import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.enums.Category;
import br.com.ticket.microservice.core.enums.Priority;
import br.com.ticket.microservice.core.enums.Status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketGateway {

    TicketDomain save(TicketDomain ticketDomain);
    boolean existsById(UUID uuid);
    boolean existsByTechnicianId(UUID technicianId);
    boolean existsByClientId(UUID clientId);
    boolean existsByIdAndStatus(UUID uuid, Status status);
    boolean existsByIdAndCategory(UUID uuid, Category category);
    boolean existsByIdAndPriority(UUID uuid, Priority priority);
    Optional<TicketDomain> findById(UUID uuid);
    List<TicketDomain> findAll();
    List<TicketDomain> findAll(Status status);
    List<TicketDomain> findAllByTechnicianId(UUID technicianId);
    List<TicketDomain> findAllByClientId(UUID clientId);

}
