package br.com.ticket.microservice.core.app.dto;

import br.com.ticket.microservice.core.enums.Category;
import br.com.ticket.microservice.core.enums.Priority;
import br.com.ticket.microservice.core.enums.Status;
import br.com.user.microservice.core.app.dto.user.UserResponseDto;
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
