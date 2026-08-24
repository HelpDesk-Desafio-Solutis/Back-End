package desk.help.infra.controllers;

import desk.help.core.app.dto.user.UserRequestDto;
import desk.help.core.app.dto.user.UserResponseDto;
import desk.help.core.app.usecases.exceptions.ErrorResponseExamples;
import desk.help.core.app.usecases.user.*;
import desk.help.core.domains.UserDomain;
import desk.help.core.enums.Role;
import desk.help.infra.mappers.UserMapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "API de gerenciamento de usuários")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final GetAllUsersByRoleUseCase getAllUsersByRoleUseCase;
    private final UpdateUserByIdUseCase updateUserByIdUseCase;
    private final DeactivateUserUseCase deactivateUserUseCase;

    @PostMapping
    @Operation(summary = "Cria um novo usuário", description = "Cria um novo usuário com base nas informações fornecidas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.CREATED)
            )),
            @ApiResponse(responseCode = "400", description = "Um ou mais campos estão inválidos", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.BAD_REQUEST)
            )),
            @ApiResponse(responseCode = "409", description = "Usuário já existe", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.CONFLICT)
            ))
    })
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto requestDto) {
        UserDomain user = UserMapper.toDomain(requestDto);
        UserDomain createdUser = createUserUseCase.execute(user);
        return new ResponseEntity<>(UserMapper.toResponseDto(createdUser), HttpStatusCode.valueOf(201));
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Buscar todos os usuários", description = "Retorna todos os usuários cadastrados no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários encontrados com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.OK)
            )),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.UNAUTHORIZED)
            )),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.FORBIDDEN)
            )),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.NOT_FOUND)
            ))
    })
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserDomain> users = getAllUsersUseCase.execute();
        List<UserResponseDto> responseDtos = users.stream()
                .map(UserMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/role/{role}")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Buscar usuários por cargo", description = "Retorna todos os usuários com base no cargo fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários encontrados com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.OK)
            )),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.UNAUTHORIZED)
            )),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.FORBIDDEN)
            )),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado com o cargo fornecido", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.NOT_FOUND)
            ))
    })
    public ResponseEntity<List<UserResponseDto>> getAllUsersByRole(@PathVariable Role role) {
        List<UserDomain> users = getAllUsersByRoleUseCase.execute(role);
        List<UserResponseDto> responseDtos = users.stream()
                .map(UserMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico com base no ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.OK)
            )),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.UNAUTHORIZED)
            )),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.FORBIDDEN)
            )),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.NOT_FOUND)
            ))
    })
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) {
        UserDomain user = getUserByIdUseCase.execute(id);
        return ResponseEntity.ok(UserMapper.toResponseDto(user));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Atualizar usuário por ID", description = "Atualiza as informações de um usuário específico com base no ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.OK)
            )),
            @ApiResponse(responseCode = "400", description = "Um ou mais campos estão inválidos", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.BAD_REQUEST)
            )),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.UNAUTHORIZED)
            )),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.FORBIDDEN)
            )),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.NOT_FOUND)
            ))
    })
    public ResponseEntity<UserResponseDto> updateUserById(@Valid @RequestBody UserRequestDto requestDto, @PathVariable UUID id) {
        UserDomain user = UserMapper.toDomain(requestDto);
        UserDomain updatedUser = updateUserByIdUseCase.execute(user, id);
        return ResponseEntity.ok(UserMapper.toResponseDto(updatedUser));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Desativar usuário por ID", description = "Desativa um usuário específico com base no ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário desativado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.NO_CONTENT)
            )),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.UNAUTHORIZED)
            )),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.FORBIDDEN)
            )),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(value = ErrorResponseExamples.NOT_FOUND)
            ))
    })
    public void deactivateUserById(@PathVariable UUID id) {
        deactivateUserUseCase.execute(id);
    }

}
