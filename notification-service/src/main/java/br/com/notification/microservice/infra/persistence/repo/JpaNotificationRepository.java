package br.com.notification.microservice.infra.persistence.repo;

import br.com.notification.microservice.infra.persistence.entity.NotificationJpaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaNotificationRepository extends JpaRepository<NotificationJpaEntity, UUID> {

    List<NotificationJpaEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

}