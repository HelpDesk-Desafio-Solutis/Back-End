package br.com.notification.microservice.core.gateway;

import br.com.notification.microservice.core.domain.NotificationDomain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationGateway {

    NotificationDomain save(NotificationDomain notification);

    Optional<NotificationDomain> findById(UUID uuid);

    List<NotificationDomain> findAll();
}