package desk.help.infra.persistence.repos;

import desk.help.core.enums.Category;
import desk.help.core.enums.Priority;
import desk.help.core.enums.Status;
import desk.help.infra.persistence.entity.TicketJpaEntity;
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
    Page<TicketJpaEntity> findAllByCategory(Category category, Pageable pageable);
    Page<TicketJpaEntity> findAllByPriority(Priority priority, Pageable pageable);
    Page<TicketJpaEntity> findAllByClientUuidAndStatus(UUID clientUuid, Status status, Pageable pageable);
    Page<TicketJpaEntity> findAllByTechnicianUuidAndStatus(UUID technicianUuid, Status status, Pageable pageable);

}
