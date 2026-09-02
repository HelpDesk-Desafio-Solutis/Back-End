package br.com.ticket.microservice.infra.controller;

import br.com.shared.exceptions.ErrorResponseExamples;
import br.com.ticket.microservice.core.app.dto.*;
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
    private final CreateTicketAdminUseCase createTicketAdminUseCase;
    private final DeactivateTicketByIdUseCase deactivateTicketByIdUseCase;
    private final GetAllTicketUseCase getAllTicketUseCase;
    private final GetAllTicketByClientUseCase getAllTicketByClientUseCase;
    private final GetTicketByIdUseCase getTicketByIdUseCase;
    private final UpdateTicketByIdUseCase updateTicketByIdUseCase;

    @PostMapping
    @Operation(summary = "Cria um novo ticket.", description = "Cria um novo ticket com base nas informações fornecidas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket criado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketResponseDto.class), examples = @ExampleObject(value = ErrorResponseExamples.CREATED))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.BAD_REQUEST)))
    })
    public ResponseEntity<TicketResponseDto> createTicket(@RequestHeader("Authorization") String authorization, @RequestHeader("X-User-UUID") UUID userUuid, @Valid @RequestBody TicketRequestDto requestDto)
        throws Exception {
            TicketDomain ticket = TicketMapper.toDomain(requestDto);
            TicketDomain createdTicket = createTicketUseCase.execute(ticket, authorization, userUuid);

            return new ResponseEntity<>(TicketMapper.toResponseDto(createdTicket), HttpStatus.CREATED);
    }

    @PostMapping("/admin")
    @Operation(summary = "Cria um novo ticket como administrador.", description = "Cria um novo ticket com base nas informações fornecidas, incluindo a possibilidade de atribuir um técnico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket criado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketResponseDto.class), examples = @ExampleObject(value = ErrorResponseExamples.CREATED))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.BAD_REQUEST)))
    })
    public ResponseEntity<TicketResponseDto> createTicketAdmin(@RequestHeader("Authorization") String authorization, @Valid @RequestBody TicketAdminRequestDto requestDto) throws Exception {
        TicketDomain ticket = TicketMapper.toDomain(requestDto);
        TicketDomain createdTicket = createTicketAdminUseCase.execute(ticket, authorization);

        return new ResponseEntity<>(TicketMapper.toResponseDto(createdTicket), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtém todos os tickets ticket pelo Status.", description = "Retorna os detalhes de todos os tickets com base no status fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tickets obtidos com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketResponseDto.class), examples = @ExampleObject(value = ErrorResponseExamples.OK))),
            @ApiResponse(responseCode = "404", description = "Tickets não encontrados.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.NOT_FOUND))),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "403", description = "Acesso proibido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.FORBIDDEN)))
    })
    public ResponseEntity<List<TicketResponseDto>> getAllTickets(
            @RequestHeader("Authorization") String authorization,
            @RequestParam(required = false) Status status) {

        Status statusEnum = null;
        if (status != null && !status.toString().isEmpty() && !status.toString().equalsIgnoreCase("TODOS")) {
            try {
                statusEnum = Status.valueOf(status.toString().toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid status value: {}", status);
            }
        }

        List<TicketDomain> tickets = getAllTicketUseCase.execute(statusEnum, authorization);
        List<TicketResponseDto> ticketDtos = tickets.stream().map(TicketMapper::toResponseDto).toList();
        return ResponseEntity.ok(ticketDtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtém um ticket pelo ID.", description = "Retorna os detalhes de um ticket específico com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket obtido com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketResponseDto.class), examples = @ExampleObject(value = ErrorResponseExamples.OK))),
            @ApiResponse(responseCode = "404", description = "Ticket não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.NOT_FOUND))),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "403", description = "Acesso proibido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.FORBIDDEN)))
    })
    public ResponseEntity<TicketResponseDto> getTicketById(@RequestHeader("Authorization") String authorization, @PathVariable UUID id) {
        TicketDomain ticket = getTicketByIdUseCase.execute(id, authorization);
        return ResponseEntity.ok(TicketMapper.toResponseDto(ticket));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um ticket pelo ID.", description = "Atualiza as informações de um ticket específico com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket atualizado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TicketResponseDto.class), examples = @ExampleObject(value = ErrorResponseExamples.OK))),
            @ApiResponse(responseCode = "404", description = "Ticket não encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.NOT_FOUND))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.BAD_REQUEST))),
            @ApiResponse(responseCode = "403", description = "Acesso proibido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseExamples.class), examples = @ExampleObject(value = ErrorResponseExamples.FORBIDDEN)))
    })
    public ResponseEntity<TicketResumedResponseDto> updateTicketById( @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody TicketUpdateDto updateDto, @PathVariable UUID id) {
        TicketDomain ticket = TicketMapper.toDomain(updateDto);
        TicketDomain updatedTicket = updateTicketByIdUseCase.execute(ticket, id, authorization);
        return ResponseEntity.ok(TicketMapper.toResumedResponseDto(updatedTicket));
    }

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
