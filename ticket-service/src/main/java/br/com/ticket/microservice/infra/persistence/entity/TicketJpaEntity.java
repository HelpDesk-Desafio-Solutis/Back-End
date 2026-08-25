package br.com.ticket.microservice.infra.persistence.entity;

import br.com.ticket.microservice.core.enums.Category;
import br.com.ticket.microservice.core.enums.Priority;
import br.com.ticket.microservice.core.enums.Status;
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
@Table(name = "tickets")
public class TicketJpaEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID uuid;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @JoinColumn(name = "client_id", nullable = false)
    private UUID clientUuid;

    @JoinColumn(name = "technician_id")
    private UUID technicianUuid;

}
