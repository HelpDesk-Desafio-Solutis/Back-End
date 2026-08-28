package br.com.user.microservice.infra.controllers;

import br.com.user.microservice.core.app.dto.user.UserRequestDto;
import br.com.user.microservice.core.app.usecases.*;
import br.com.user.microservice.core.domain.UserDomain;
import br.com.user.microservice.core.enums.Role;
import br.com.user.microservice.infra.config.auth.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@Import(br.com.user.microservice.infra.config.auth.SecurityConfig.class)
@TestPropertySource(properties = {
        "web-endpoint.url=http://localhost:3000"
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    @MockitoBean
    private GetUserByIdUseCase getUserByIdUseCase;

    @MockitoBean
    private GetAllUsersUseCase getAllUsersUseCase;

    @MockitoBean
    private GetAllUsersByRoleUseCase getAllUsersByRoleUseCase;

    @MockitoBean
    private UpdateUserByIdUseCase updateUserByIdUseCase;

    @MockitoBean
    private DeactivateUserUseCase deactivateUserUseCase;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void deveCriarUsuarioComSucesso() throws Exception {

        UUID uuid = UUID.randomUUID();

        UserRequestDto request = UserRequestDto.builder()
                .name("João da Silva")
                .email("joao@email.com")
                .password("123456")
                .role(Role.CLIENT)
                .build();

        UserDomain createdUser = new UserDomain(
                uuid,
                true,
                null,
                null,
                "João da Silva",
                "joao@email.com",
                "123456",
                Role.CLIENT
        );

        when(createUserUseCase.execute(any(UserDomain.class)))
                .thenReturn(createdUser);

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(uuid.toString()))
                .andExpect(jsonPath("$.name").value("João da Silva"))
                .andExpect(jsonPath("$.email").value("joao@email.com"))
                .andExpect(jsonPath("$.role").value("CLIENT"));

        verify(createUserUseCase).execute(any(UserDomain.class));
    }


    @Test
    void deveRetornar400QuandoNomeForVazio() throws Exception {

        UserRequestDto request = UserRequestDto.builder()
                .name("")
                .email("joao@email.com")
                .password("123456")
                .role(Role.CLIENT)
                .build();

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(createUserUseCase);
    }


    @Test
    void deveRetornar400QuandoEmailForInvalido() throws Exception {

        UserRequestDto request = UserRequestDto.builder()
                .name("João da Silva")
                .email("email-invalido")
                .password("123456")
                .role(Role.CLIENT)
                .build();

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(createUserUseCase);
    }


    @Test
    void deveRetornar400QuandoSenhaForMenorQueSeisCaracteres() throws Exception {

        UserRequestDto request = UserRequestDto.builder()
                .name("João da Silva")
                .email("joao@email.com")
                .password("123")
                .role(Role.CLIENT)
                .build();

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(createUserUseCase);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveBuscarTodosOsUsuarios() throws Exception {

        UUID uuid = UUID.randomUUID();

        UserDomain user = new UserDomain(
                uuid,
                true,
                null,
                null,
                "João da Silva",
                "joao@email.com",
                "123456",
                Role.CLIENT
        );

        when(getAllUsersUseCase.execute())
                .thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].uuid").value(uuid.toString()))
                .andExpect(jsonPath("$[0].name").value("João da Silva"))
                .andExpect(jsonPath("$[0].email").value("joao@email.com"))
                .andExpect(jsonPath("$[0].role").value("CLIENT"));

        verify(getAllUsersUseCase).execute();
    }


    @Test
    void deveRetornar401AoBuscarUsuariosSemAutenticacao() throws Exception {

        mockMvc.perform(get("/users"))
                .andExpect(status().isUnauthorized());

        verifyNoMoreInteractions(getAllUsersUseCase);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveBuscarUsuariosPorRole() throws Exception {

        UUID uuid = UUID.randomUUID();

        UserDomain user = new UserDomain(
                uuid,
                true,
                null,
                null,
                "João da Silva",
                "joao@email.com",
                "123456",
                Role.CLIENT
        );

        when(getAllUsersByRoleUseCase.execute(Role.CLIENT))
                .thenReturn(List.of(user));

        mockMvc.perform(get("/users/role/CLIENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].uuid").value(uuid.toString()))
                .andExpect(jsonPath("$[0].name").value("João da Silva"))
                .andExpect(jsonPath("$[0].email").value("joao@email.com"))
                .andExpect(jsonPath("$[0].role").value("CLIENT"));

        verify(getAllUsersByRoleUseCase).execute(Role.CLIENT);
    }


    @Test
    void deveRetornar401AoBuscarUsuariosPorRoleSemAutenticacao() throws Exception {

        mockMvc.perform(get("/users/role/CLIENT"))
                .andExpect(status().isUnauthorized());

        verifyNoMoreInteractions(getAllUsersByRoleUseCase);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveBuscarUsuarioPorId() throws Exception {

        UUID uuid = UUID.randomUUID();

        UserDomain user = new UserDomain(
                uuid,
                true,
                null,
                null,
                "João da Silva",
                "joao@email.com",
                "123456",
                Role.CLIENT
        );

        when(getUserByIdUseCase.execute(uuid))
                .thenReturn(user);

        mockMvc.perform(get("/users/{id}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(uuid.toString()))
                .andExpect(jsonPath("$.name").value("João da Silva"))
                .andExpect(jsonPath("$.email").value("joao@email.com"))
                .andExpect(jsonPath("$.role").value("CLIENT"));

        verify(getUserByIdUseCase).execute(uuid);
    }


    @Test
    void deveRetornar401AoBuscarUsuarioPorIdSemAutenticacao() throws Exception {

        UUID uuid = UUID.randomUUID();

        mockMvc.perform(get("/users/{id}", uuid))
                .andExpect(status().isUnauthorized());

        verifyNoMoreInteractions(getUserByIdUseCase);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveAtualizarUsuario() throws Exception {

        UUID uuid = UUID.randomUUID();

        UserRequestDto request = UserRequestDto.builder()
                .name("João Atualizado")
                .email("joao.atualizado@email.com")
                .password("123456")
                .role(Role.CLIENT)
                .build();

        UserDomain updatedUser = new UserDomain(
                uuid,
                true,
                null,
                null,
                "João Atualizado",
                "joao.atualizado@email.com",
                "123456",
                Role.CLIENT
        );

        when(updateUserByIdUseCase.execute(
                any(UserDomain.class),
                eq(uuid)
        )).thenReturn(updatedUser);

        mockMvc.perform(
                        put("/users/{id}", uuid)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(uuid.toString()))
                .andExpect(jsonPath("$.name").value("João Atualizado"))
                .andExpect(jsonPath("$.email").value("joao.atualizado@email.com"))
                .andExpect(jsonPath("$.role").value("CLIENT"));

        verify(updateUserByIdUseCase)
                .execute(any(UserDomain.class), eq(uuid));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar400AoAtualizarComEmailInvalido() throws Exception {

        UUID uuid = UUID.randomUUID();

        UserRequestDto request = UserRequestDto.builder()
                .name("João")
                .email("email-invalido")
                .password("123456")
                .role(Role.CLIENT)
                .build();

        mockMvc.perform(
                        put("/users/{id}", uuid)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verifyNoMoreInteractions(updateUserByIdUseCase);
    }


    @Test
    void deveRetornar401AoAtualizarSemAutenticacao() throws Exception {

        UUID uuid = UUID.randomUUID();

        UserRequestDto request = UserRequestDto.builder()
                .name("João")
                .email("joao@email.com")
                .password("123456")
                .role(Role.CLIENT)
                .build();

        mockMvc.perform(
                        put("/users/{id}", uuid)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isUnauthorized());

        verifyNoMoreInteractions(updateUserByIdUseCase);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveDesativarUsuario() throws Exception {

        UUID uuid = UUID.randomUUID();

        doNothing().when(deactivateUserUseCase).execute(uuid);

        mockMvc.perform(delete("/users/{id}", uuid))
                .andExpect(status().isNoContent());

        verify(deactivateUserUseCase).execute(uuid);
    }


    @Test
    void deveRetornar401AoDesativarSemAutenticacao() throws Exception {

        UUID uuid = UUID.randomUUID();

        mockMvc.perform(delete("/users/{id}", uuid))
                .andExpect(status().isUnauthorized());

        verifyNoMoreInteractions(deactivateUserUseCase);
    }
}