package br.com.ticket.microservice.core.app.dto;

import br.com.ticket.microservice.core.enums.Category;
import br.com.ticket.microservice.core.enums.Priority;
import br.com.ticket.microservice.core.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketUpdateDto {
    private Priority priority;
    private Status status;

    @NotNull(message = "Preencha com o ID do Técnico")
    private UUID technicianUuid;
}
