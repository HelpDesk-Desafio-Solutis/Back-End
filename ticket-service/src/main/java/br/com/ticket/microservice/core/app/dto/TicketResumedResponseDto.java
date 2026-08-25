package br.com.ticket.microservice.core.app.dto;

import br.com.ticket.microservice.core.enums.Category;
import br.com.ticket.microservice.core.enums.Priority;
import br.com.ticket.microservice.core.app.dto.user.UserResponseDto;
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