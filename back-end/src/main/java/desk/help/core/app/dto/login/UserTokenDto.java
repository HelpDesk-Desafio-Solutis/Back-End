package desk.help.core.app.dto.login;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserTokenDto {

    private UUID userId;
    private String nome;
    private String email;
    private String token;
    private String tipoUsuario;

}
