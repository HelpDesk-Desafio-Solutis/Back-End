package desk.help.core.gateway;

import desk.help.core.domains.TicketDomain;
import desk.help.core.enums.Category;
import desk.help.core.enums.Priority;
import desk.help.core.enums.Status;

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
