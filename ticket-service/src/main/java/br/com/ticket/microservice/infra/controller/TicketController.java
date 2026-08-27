package br.com.ticket.microservice.infra.controller;

import br.com.shared.exceptions.ErrorResponseExamples;
import br.com.ticket.microservice.core.app.dto.TicketRequestDto;
import br.com.ticket.microservice.core.app.dto.TicketResponseDto;
import br.com.ticket.microservice.core.app.dto.TicketResumedResponseDto;
import br.com.ticket.microservice.core.app.usecases.*;
import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.enums.Status;
import br.com.ticket.microservice.infra.mapper.TicketMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@Tag(name = "Ticket", description = "Endpoint de gerenciamento de tickets.")
public class TicketController {

    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    private final CreateTicketUseCase createTicketUseCase;
    private final DeactivateTicketByIdUseCase deactivateTicketByIdUseCase;
    private final GetAllTicketUseCase getAllTicketUseCase;
    private final GetAllTicketByClientUseCase getAllTicketByClientUseCase;
    private final GetTicketByIdUseCase getTicketByIdUseCase;
    private final UpdateTicketByIdUseCase updateTicketByIdUseCase;

    @SecurityRequirement(name = "Bearer")
    @PostMapping
    @Operation(summary = "Cria um novo ticket.", description = "Cria um novo ticket com base nas informações fornecidas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket criado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketResponseDto.class), examples = @ExampleObject(value = ErrorResponseExamples.CREATED))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.BAD_REQUEST)))
    })
    public ResponseEntity<TicketResponseDto> createTicket(@Valid @RequestBody TicketRequestDto requestDto)
        throws Exception {
            TicketDomain ticket = TicketMapper.toDomain(requestDto);
            TicketDomain createdTicket = createTicketUseCase.execute(ticket);

            return new ResponseEntity<>(TicketMapper.toResponseDto(createdTicket), HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "Bearer")
    @GetMapping
    @Operation(summary = "Obtém um ticket pelo ID.", description = "Retorna os detalhes de um ticket específico com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket obtido com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketResponseDto.class), examples = @ExampleObject(value = ErrorResponseExamples.OK))),
            @ApiResponse(responseCode = "404", description = "Ticket não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.NOT_FOUND))),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "403", description = "Acesso proibido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.FORBIDDEN)))
    })
    public ResponseEntity<List<TicketResponseDto>> getAllTickets(
            @RequestParam(required = false) Status status) {
        logger.info("getAllSchedules called with status: {}", status);
        Status statusEnum = null;
        if (status != null && !status.toString().isEmpty() && !status.toString().equalsIgnoreCase("TODOS")) {
            try {
                statusEnum = Status.valueOf(status.toString().toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid status value: {}", status);
            }
        }

        logger.info("GetAllTickets called with status: {}", statusEnum);

        List<TicketDomain> tickets = getAllTicketUseCase.execute(statusEnum);
        List<TicketResponseDto> ticketDtos = tickets.stream().map(TicketMapper::toResponseDto).toList();
        return ResponseEntity.ok(ticketDtos);
    }

    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    @Operation(summary = "Obtém um ticket pelo ID.", description = "Retorna os detalhes de um ticket específico com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket obtido com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketResponseDto.class), examples = @ExampleObject(value = ErrorResponseExamples.OK))),
            @ApiResponse(responseCode = "404", description = "Ticket não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.NOT_FOUND))),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "403", description = "Acesso proibido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.FORBIDDEN)))
    })
    public ResponseEntity<TicketResponseDto> getTicketById(@PathVariable UUID id) {
        TicketDomain ticket = getTicketByIdUseCase.execute(id);
        return ResponseEntity.ok(TicketMapper.toResponseDto(ticket));
    }

    @SecurityRequirement(name = "Bearer")
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um ticket pelo ID.", description = "Atualiza as informações de um ticket específico com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket atualizado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketResponseDto.class), examples = @ExampleObject(value = ErrorResponseExamples.OK))),
            @ApiResponse(responseCode = "404", description = "Ticket não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.NOT_FOUND))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "403", description = "Acesso proibido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.FORBIDDEN)))
    })
    public ResponseEntity<TicketResumedResponseDto> updateTicketById(
            @Valid @RequestBody TicketRequestDto requestDto, @PathVariable UUID id) {
        TicketDomain ticket = TicketMapper.toDomain(requestDto);
        TicketDomain updatedTicket = updateTicketByIdUseCase.execute(ticket, id);
        return ResponseEntity.ok(TicketMapper.toResumedResponseDto(updatedTicket));
    }

    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desativa um ticket pelo ID.", description = "Desativa um ticket específico com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ticket desativado com sucesso.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Ticket não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.NOT_FOUND))),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "403", description = "Acesso proibido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.FORBIDDEN)))
    })
    public void deactivateTicketById(@PathVariable UUID id) {
        deactivateTicketByIdUseCase.execute(id);
    }

    @SecurityRequirement(name = "Bearer")
    @GetMapping("/client/{id}")
    @Operation(summary = "Obtém todos os tickets de um cliente.", description = "Retorna uma lista de tickets associados a um cliente específico com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tickets obtidos com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketResponseDto.class), examples = @ExampleObject(value = ErrorResponseExamples.OK))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.NOT_FOUND))),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "403", description = "Acesso proibido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.FORBIDDEN)))
    })
    public ResponseEntity<List<TicketResponseDto>> getAllTicketByClient(@PathVariable UUID id) {
        List<TicketDomain> tickets = getAllTicketByClientUseCase.execute(id);
        List<TicketResponseDto> ticketDtos = tickets.stream().map(TicketMapper::toResponseDto).toList();
        return ResponseEntity.ok(ticketDtos);
    }

}
