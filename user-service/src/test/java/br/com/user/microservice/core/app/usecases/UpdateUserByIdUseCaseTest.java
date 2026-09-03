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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserByIdUseCaseTest {

    @Mock
    private UserGateway gateway;

    @Mock
    private PasswordEncoderGateway encoderGateway;

    @InjectMocks
    private UpdateUserByIdUseCase useCase;

    @Test
    void deveAtualizarUsuarioComSucesso() {

        UUID uuid = UUID.randomUUID();

        UserDomain existingUser = new UserDomain(
                uuid,
                true,
                null,
                null,
                "João da Silva",
                "joao@email.com",
                "123456",
                Role.CLIENT
        );

        UserDomain userToUpdate = new UserDomain(
                null,
                true,
                null,
                null,
                "João Atualizado",
                "joao.atualizado@email.com",
                "123456",
                Role.CLIENT
        );

        when(gateway.existsById(uuid))
                .thenReturn(true);

        when(gateway.existsByIdAndActiveFalse(uuid))
                .thenReturn(false);

        when(gateway.existsByIdNotAndEmailIgnoreCase(
                uuid,
                userToUpdate.getEmail()
        )).thenReturn(false);

        when(gateway.findById(uuid))
                .thenReturn(Optional.of(existingUser));

        when(encoderGateway.encode("123456"))
                .thenReturn("senha-criptografada");

        when(gateway.save(any(UserDomain.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserDomain result =
                useCase.execute(userToUpdate, uuid);

        assertNotNull(result);
        assertEquals(uuid, result.getUuid());

        assertEquals(
                "João Atualizado",
                result.getName()
        );

        assertEquals(
                "joao.atualizado@email.com",
                result.getEmail()
        );

        assertEquals(
                "senha-criptografada",
                result.getPassword()
        );

        assertEquals(
                existingUser.getCreatedAt(),
                result.getCreatedAt()
        );

        assertNotNull(result.getUpdatedAt());

        verify(gateway).existsById(uuid);

        verify(gateway).existsByIdAndActiveFalse(uuid);

        verify(gateway).existsByIdNotAndEmailIgnoreCase(
                uuid,
                userToUpdate.getEmail()
        );

        verify(gateway).findById(uuid);

        verify(encoderGateway).encode("123456");

        verify(gateway).save(userToUpdate);
    }
}