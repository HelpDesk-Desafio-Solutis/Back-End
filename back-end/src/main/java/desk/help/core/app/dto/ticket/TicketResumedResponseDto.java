package desk.help.core.app.dto.ticket;

import desk.help.core.app.dto.user.UserResponseDto;
import desk.help.core.enums.Category;
import desk.help.core.enums.Priority;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResumedResponseDto {

    private UUID uuid;
    private String title;
    private Category category;
    private Priority priority;
    private UserResponseDto client;
    private UserResponseDto technician;

}