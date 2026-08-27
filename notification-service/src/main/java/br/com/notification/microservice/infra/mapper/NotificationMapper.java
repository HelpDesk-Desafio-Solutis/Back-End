package br.com.notification.microservice.infra.mapper;

import br.com.notification.microservice.core.domain.NotificationDomain;
import br.com.notification.microservice.infra.persistence.entity.NotificationJpaEntity;

public class NotificationMapper {

    private NotificationMapper() {
    }

    public static NotificationDomain toDomain(NotificationJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        NotificationDomain domain = new NotificationDomain();

        domain.setUuid(entity.getUuid());
        domain.setTicketUuid(entity.getTicketUuid());
        domain.setUserUuid(entity.getUserUuid());
        domain.setEmail(entity.getEmail());
        domain.setMessage(entity.getMessage());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setStatus(entity.getStatus());

        return domain;
    }

    public static NotificationJpaEntity toJpaEntity(NotificationDomain domain) {
        if (domain == null) {
            return null;
        }

        NotificationJpaEntity entity = new NotificationJpaEntity();

        entity.setUuid(domain.getUuid());
        entity.setTicketUuid(domain.getTicketUuid());
        entity.setUserUuid(domain.getUserUuid());
        entity.setEmail(domain.getEmail());
        entity.setMessage(domain.getMessage());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setStatus(domain.getStatus());

        return entity;
    }
}