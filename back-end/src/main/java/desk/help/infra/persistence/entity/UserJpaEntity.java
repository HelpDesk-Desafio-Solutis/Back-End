package desk.help.infra.persistence.entity;

import desk.help.core.enums.Role;
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
@Table(name = "users")
public class UserJpaEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID uuid;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

}
