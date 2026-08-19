package desk.help.core.app.dto.user;

import desk.help.core.enums.Role;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    @Size(max = 80, message = "Nome muito longo")
    @NotBlank(message = "Insira o nome")
    private String name;

    @Size(max = 80, message = "E-mail muito longo")
    @Email(message = "Valor inserido não é um e-mail")
    @NotBlank(message = "Insira um e-mail")
    private String email;

    @Positive(message = "Cargo inválido")
    @NotNull(message = "Insira o cargo do usuário")
    private Role role;

}
