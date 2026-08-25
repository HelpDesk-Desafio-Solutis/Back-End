package br.com.ticket.microservice.infra.persistence.entity;

import br.com.ticket.microservice.core.enums.Category;
import br.com.ticket.microservice.core.enums.Priority;
import br.com.ticket.microservice.core.enums.Status;
import br.com.user.microservice.infra.persistence.entity.UserJpaEntity;
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

    @ManyToOne
    @JoinColumn(name = "fk_client")
    private UserJpaEntity client;

    @ManyToOne
    @JoinColumn(name = "fk_technician")
    private UserJpaEntity technician;

}
