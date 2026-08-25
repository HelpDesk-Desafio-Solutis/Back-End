package br.com.ticket.microservice.infra.persistence.adapter;

import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.enums.Category;
import br.com.ticket.microservice.core.enums.Priority;
import br.com.ticket.microservice.core.enums.Status;
import br.com.ticket.microservice.core.gateway.TicketGateway;
import br.com.ticket.microservice.infra.mapper.TicketMapper;
import br.com.ticket.microservice.infra.persistence.entity.TicketJpaEntity;
import br.com.ticket.microservice.infra.persistence.repo.JpaTicketRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketAdapter implements TicketGateway {

    private static final Logger logger = LoggerFactory.getLogger(TicketAdapter.class);
    private final JpaTicketRepository repository;

    @Override
    public TicketDomain save(TicketDomain ticket) {
        TicketJpaEntity jpaEntity = TicketMapper.toJpaEntity(ticket);
        TicketJpaEntity savedEntity = repository.save(jpaEntity);
        return TicketMapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return repository.existsById(uuid);
    }

    @Override
    public boolean existsByTechnicianId(UUID technicianId) {
        return repository.existsByTechnicianUuid(technicianId);
    }

    @Override
    public boolean existsByClientId(UUID clientId) {
        return repository.existsByClientUuid(clientId);
    }

    @Override
    public boolean existsByIdAndStatus(UUID uuid, Status status) {
        return repository.existsByUuidAndStatus(uuid, status);
    }

    @Override
    public boolean existsByIdAndCategory(UUID uuid, Category category) {
        return repository.existsByUuidAndCategory(uuid, category);
    }

    @Override
    public boolean existsByIdAndPriority(UUID uuid, Priority priority) {
        return repository.existsByUuidAndPriority(uuid, priority);
    }

    @Override
    public Optional<TicketDomain> findById(UUID uuid) {
        return repository.findById(uuid).map(TicketMapper::toDomain);
    }

    @Override
    public List<TicketDomain> findAll() {
        return repository.findAll().stream()
                .map(TicketMapper::toDomain)
                .toList();
    }

    @Override
    public List<TicketDomain> findAll(Status status) {
        logger.info("findAll - status: {}", status);

        // Filtro de status
        if (status != null) {
            logger.info("Applying status only filter");
            return repository.findAllByStatus(status).stream()
                    .map(TicketMapper::toDomain)
                    .collect(Collectors.toList());
        } else {
            logger.info("No status filter applied");
            return repository.findAll().stream()
                    .map(TicketMapper::toDomain)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<TicketDomain> findAllByTechnicianId(UUID technicianId) {
        return repository.findAllByTechnicianUuid(technicianId).stream()
                .map(TicketMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDomain> findAllByClientId(UUID clientId) {
        return repository.findAllByClientUuid(clientId).stream()
                .map(TicketMapper::toDomain)
                .collect(Collectors.toList());
    }

}
