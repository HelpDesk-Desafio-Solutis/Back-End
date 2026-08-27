package br.com.notification.microservice.infra.controller;

import br.com.notification.microservice.core.app.dto.NotificationResponseDto;
import br.com.notification.microservice.core.app.usecases.GetAllNotificationUseCase;
import br.com.notification.microservice.core.app.usecases.GetNotificationByIdUseCase;
import br.com.shared.exceptions.ErrorResponseExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(
        name = "Notifications",
        description = "Endpoints para consulta das notificações"
)
public class NotificationController {

    private final GetAllNotificationUseCase getAllNotificationUseCase;
    private final GetNotificationByIdUseCase getNotificationByIdUseCase;


    @SecurityRequirement(name = "Bearer")
    @GetMapping
    @Operation(summary = "Listar todas as notificações", description = "Retorna uma lista de todas as notificações disponíveis.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de notificações retornada com sucesso.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = NotificationResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.OK)
                    )),
            @ApiResponse(responseCode = "401", description = "Não autorizado. Token de autenticação inválido ou ausente.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = NotificationResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.UNAUTHORIZED)
            )),
            @ApiResponse(responseCode = "403", description = "Proibido. O usuário não tem permissão para acessar este recurso.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = NotificationResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.FORBIDDEN)))
    })
    public ResponseEntity<List<NotificationResponseDto>> getAllNotifications(@RequestHeader("Authorization") String authorizationHeader) {

        List<NotificationResponseDto> notifications =
                getAllNotificationUseCase.execute(authorizationHeader);

        return ResponseEntity.ok(notifications);
    }


    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    @Operation(summary = "Obter notificação por ID", description = "Retorna uma notificação específica com base no ID fornecido.")
    @ApiResponses({
                    @ApiResponse(responseCode = "200", description = "Notificação retornada com sucesso.", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponseDto.class),
                            examples = @ExampleObject(value = ErrorResponseExamples.OK)
                    )),
                    @ApiResponse(responseCode = "401", description = "Não autorizado. Token de autenticação inválido ou ausente.", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponseDto.class),
                            examples = @ExampleObject(value = ErrorResponseExamples.UNAUTHORIZED)
                    )),
                    @ApiResponse(responseCode = "403", description = "Proibido. O usuário não tem permissão para acessar este recurso.", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponseDto.class),
                            examples = @ExampleObject(value = ErrorResponseExamples.FORBIDDEN)))
            })
    public ResponseEntity<NotificationResponseDto> getNotificationById(@PathVariable UUID id, @RequestHeader("Authorization") String authorizationHeader) {
        return getNotificationByIdUseCase.execute(id, authorizationHeader)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}