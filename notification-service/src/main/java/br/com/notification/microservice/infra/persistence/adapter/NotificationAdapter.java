package br.com.notification.microservice.infra.persistence.adapter;

import br.com.notification.microservice.core.domain.NotificationDomain;
import br.com.notification.microservice.core.gateway.NotificationGateway;
import br.com.notification.microservice.infra.mapper.NotificationMapper;
import br.com.notification.microservice.infra.persistence.entity.NotificationJpaEntity;
import br.com.notification.microservice.infra.persistence.repo.JpaNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationAdapter implements NotificationGateway {

    private final JpaNotificationRepository repository;

    @Override
    public NotificationDomain save(NotificationDomain notification) {
        NotificationJpaEntity entity =
                NotificationMapper.toJpaEntity(notification);

        NotificationJpaEntity savedEntity =
                repository.save(entity);

        System.out.println(
                "ENTITY CLIENT UUID: "
                        + entity.getClientUuid()
        );

        return NotificationMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<NotificationDomain> findById(UUID uuid) {
        return repository.findById(uuid)
                .map(NotificationMapper::toDomain);
    }

    @Override
    public List<NotificationDomain> findAll() {
        return repository.findAll().stream()
                .map(NotificationMapper::toDomain)
                .toList();
    }
}