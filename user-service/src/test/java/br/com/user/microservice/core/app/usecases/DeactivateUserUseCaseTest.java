package br.com.user.microservice.core.app.usecases;

import br.com.shared.exceptions.exceptionClass.InactiveEntityException;
import br.com.user.microservice.core.domain.UserDomain;
import br.com.user.microservice.core.gateway.UserGateway;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeactivateUserUseCaseTest {

    @Mock
    private UserGateway gateway;

    private DeactivateUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeactivateUserUseCase(gateway);
    }

    @Test
    void shouldDeactivateUserSuccessfully() {

        UUID uuid = UUID.randomUUID();

        UserDomain user = new UserDomain();
        user.setUuid(uuid);
        user.setActive(true);

        when(gateway.existsById(uuid))
                .thenReturn(true);

        when(gateway.existsByIdAndActiveFalse(uuid))
                .thenReturn(false);

        when(gateway.findById(uuid))
                .thenReturn(Optional.of(user));

        useCase.execute(uuid);

        assertFalse(user.isActive());
        assertNotNull(user.getUpdatedAt());

        verify(gateway).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        UUID uuid = UUID.randomUUID();

        when(gateway.existsById(uuid))
                .thenReturn(false);

        assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(uuid)
        );

        verify(gateway, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserIsAlreadyInactive() {

        UUID uuid = UUID.randomUUID();

        when(gateway.existsById(uuid))
                .thenReturn(true);

        when(gateway.existsByIdAndActiveFalse(uuid))
                .thenReturn(true);

        assertThrows(
                InactiveEntityException.class,
                () -> useCase.execute(uuid)
        );

        verify(gateway, never()).save(any());
    }
}