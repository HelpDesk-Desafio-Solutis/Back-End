package br.com.ticket.microservice.infra.persistence.repo;

import br.com.ticket.microservice.core.enums.Category;
import br.com.ticket.microservice.core.enums.Priority;
import br.com.ticket.microservice.core.enums.Status;
import br.com.ticket.microservice.infra.persistence.entity.TicketJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaTicketRepository extends JpaRepository<TicketJpaEntity, UUID> {

    boolean existsByTechnicianUuid(UUID technicianId);
    boolean existsByClientUuid(UUID clientId);
    boolean existsByUuidAndStatus(UUID uuid, Status status);
    boolean existsByUuidAndCategory(UUID uuid, Category category);
    boolean existsByUuidAndPriority(UUID uuid, Priority priority);
    List<TicketJpaEntity> findAllByClientUuid(UUID clientUuid);
    List<TicketJpaEntity> findAllByTechnicianUuid(UUID technicianUuid);
    List<TicketJpaEntity> findAllByStatus(Status status);
    List<TicketJpaEntity> findAllByStatusAndTechnicianUuidIsNull(Status status);
}
