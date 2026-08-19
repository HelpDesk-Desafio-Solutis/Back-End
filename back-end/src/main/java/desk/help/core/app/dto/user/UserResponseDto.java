package desk.help.core.app.dto.user;

import desk.help.core.enums.Role;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private UUID uuid;
    private String name;
    private String email;
    private Role role;

}
