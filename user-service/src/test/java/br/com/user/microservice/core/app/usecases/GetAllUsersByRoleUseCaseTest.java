package br.com.user.microservice.core.app.usecases;

import br.com.user.microservice.core.domain.UserDomain;
import br.com.user.microservice.core.enums.Role;
import br.com.user.microservice.core.gateway.UserGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllUsersByRoleUseCaseTest {

    @Mock
    private UserGateway gateway;

    @InjectMocks
    private GetAllUsersByRoleUseCase useCase;

    @Test
    void deveRetornarUsuariosPorRole() {

        UserDomain user = new UserDomain(
                UUID.randomUUID(),
                true,
                null,
                null,
                "João da Silva",
                "joao@email.com",
                "123456",
                Role.CLIENT
        );

        when(gateway.findAllByActiveTrueAndRole(Role.CLIENT))
                .thenReturn(List.of(user));

        List<UserDomain> result =
                useCase.execute(Role.CLIENT);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
        assertEquals(Role.CLIENT, result.get(0).getRole());

        verify(gateway)
                .findAllByActiveTrueAndRole(Role.CLIENT);
    }
}