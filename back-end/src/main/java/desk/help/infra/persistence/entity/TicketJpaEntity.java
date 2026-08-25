package desk.help.infra.persistence.entity;

import desk.help.core.enums.Category;
import desk.help.core.enums.Priority;
import desk.help.core.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
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
