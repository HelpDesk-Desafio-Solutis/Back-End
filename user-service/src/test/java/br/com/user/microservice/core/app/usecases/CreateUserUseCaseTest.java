package br.com.user.microservice.core.app.usecases;

import br.com.shared.gateway.PasswordEncoderGateway;
import br.com.user.microservice.core.domain.UserDomain;
import br.com.user.microservice.core.enums.Role;
import br.com.user.microservice.core.gateway.UserGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserGateway gateway;

    @Mock
    private PasswordEncoderGateway encoderGateway;

    @InjectMocks
    private CreateUserUseCase useCase;

    @Test
    void deveCriarUsuarioComSucesso() {

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

        when(gateway.existsByEmailIgnoreCase(user.getEmail()))
                .thenReturn(false);

        when(encoderGateway.encode("123456"))
                .thenReturn("senha-criptografada");

        when(gateway.save(any(UserDomain.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserDomain result = useCase.execute(user);

        assertNotNull(result);
        assertNull(result.getUuid());
        assertEquals("João da Silva", result.getName());
        assertEquals("joao@email.com", result.getEmail());
        assertEquals(Role.CLIENT, result.getRole());
        assertEquals("senha-criptografada", result.getPassword());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        verify(gateway).existsByEmailIgnoreCase(user.getEmail());
        verify(encoderGateway).encode("123456");
        verify(gateway).save(user);
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaEstiverCadastrado() {

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

        when(gateway.existsByEmailIgnoreCase(user.getEmail()))
                .thenReturn(true);

        assertThrows(
                br.com.shared.exceptions.ConflictException.class,
                () -> useCase.execute(user)
        );

        verify(gateway).existsByEmailIgnoreCase(user.getEmail());

        verify(encoderGateway, never())
                .encode(anyString());

        verify(gateway, never())
                .save(any(UserDomain.class));
    }
}