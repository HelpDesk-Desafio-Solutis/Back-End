package br.com.user.microservice.core.app.dto.user;

import br.com.user.microservice.core.enums.Role;
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
