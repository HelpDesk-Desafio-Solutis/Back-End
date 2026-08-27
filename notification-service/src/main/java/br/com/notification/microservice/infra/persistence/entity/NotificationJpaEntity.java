package br.com.notification.microservice.infra.persistence.entity;

import br.com.notification.microservice.core.enums.Status;
import br.com.notification.microservice.core.enums.Type;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class NotificationJpaEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID uuid;

    @JoinColumn(name = "ticket_id", nullable = false)
    private UUID ticketUuid;

    @Column(name = "client_id", nullable = false)
    private UUID clientUuid;

    @Column(name = "technician_id")
    private UUID technicianUuid;

    private String email;
    private String message;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Type type;

}
