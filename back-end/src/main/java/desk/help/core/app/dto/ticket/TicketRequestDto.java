package desk.help.core.app.dto.ticket;

import desk.help.core.app.dto.user.UserResponseDto;
import desk.help.core.enums.Category;
import desk.help.core.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequestDto {

    @Size(max = 80, message = "Título muito longo")
    @NotBlank(message = "Insira o título do chamado")
    private String title;

    @Size(max = 250, message = "Descrição muito longa")
    @NotBlank(message = "Insira a descrição do chamado")
    private String description;

    private Category category;
    private Priority priority;

    @Positive(message = "ID inválido para Cliente")
    @NotNull(message = "Preencha com o ID do Cliente")
    private UUID clientUuid;

    @Positive(message = "ID inválido para Técnico")
    @NotNull(message = "Preencha com o ID do Técnico")
    private UUID technicianUuid;
}
