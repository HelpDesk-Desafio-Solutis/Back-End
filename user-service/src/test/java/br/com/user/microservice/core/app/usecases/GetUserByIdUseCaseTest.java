package br.com.user.microservice.core.app.usecases;

import br.com.shared.exceptions.exceptionClass.EntityNotFoundException;
import br.com.user.microservice.core.domain.UserDomain;
import br.com.user.microservice.core.enums.Role;
import br.com.user.microservice.core.gateway.UserGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserByIdUseCaseTest {

    @Mock
    private UserGateway gateway;

    @InjectMocks
    private GetUserByIdUseCase useCase;

    @Test
    void deveBuscarUsuarioPorId() {

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

        when(gateway.findByIdAndActiveTrue(uuid))
                .thenReturn(Optional.of(user));

        UserDomain result = useCase.execute(uuid);

        assertNotNull(result);
        assertEquals(uuid, result.getUuid());
        assertEquals("João da Silva", result.getName());
        assertEquals("joao@email.com", result.getEmail());

        verify(gateway).findByIdAndActiveTrue(uuid);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForEncontrado() {

        UUID uuid = UUID.randomUUID();

        when(gateway.findByIdAndActiveTrue(uuid))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(uuid)
        );

        verify(gateway).findByIdAndActiveTrue(uuid);
    }
}