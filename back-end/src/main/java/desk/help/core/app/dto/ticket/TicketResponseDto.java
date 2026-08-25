package desk.help.core.app.dto.ticket;

import desk.help.core.app.dto.user.UserResponseDto;
import desk.help.core.enums.Category;
import desk.help.core.enums.Priority;
import desk.help.core.enums.Status;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseDto {

    private UUID uuid;
    private String title;
    private String description;
    private Category category;
    private Status status;
    private Priority priority;
    private UserResponseDto client;
    private UserResponseDto technician;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
