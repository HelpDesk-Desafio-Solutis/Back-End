package br.com.ticket.microservice.core.app.usecases;

import br.com.shared.exceptions.exceptionClass.InactiveEntityException;
import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.enums.Status;
import br.com.ticket.microservice.core.gateway.TicketGateway;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeactivateTicketByIdUseCaseTest {

    @Mock
    private TicketGateway gateway;

    private DeactivateTicketByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeactivateTicketByIdUseCase(gateway);
    }

    @Test
    void shouldDeactivateTicket() {

        UUID uuid = UUID.randomUUID();

        TicketDomain ticket = mock(TicketDomain.class);

        when(gateway.existsById(uuid))
                .thenReturn(true);

        when(gateway.existsByIdAndStatus(uuid, Status.CLOSED))
                .thenReturn(false);

        when(gateway.findById(uuid))
                .thenReturn(Optional.of(ticket));

        assertDoesNotThrow(() -> useCase.execute(uuid));

        verify(ticket).setStatus(Status.CLOSED);
        verify(ticket).setUpdatedAt(any());

        verify(gateway).save(ticket);
    }

    @Test
    void shouldThrowExceptionWhenTicketDoesNotExist() {

        UUID uuid = UUID.randomUUID();

        when(gateway.existsById(uuid))
                .thenReturn(false);

        assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(uuid)
        );

        verify(gateway, never()).findById(uuid);
        verify(gateway, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenTicketIsAlreadyClosed() {

        UUID uuid = UUID.randomUUID();

        when(gateway.existsById(uuid))
                .thenReturn(true);

        when(gateway.existsByIdAndStatus(uuid, Status.CLOSED))
                .thenReturn(true);

        assertThrows(
                InactiveEntityException.class,
                () -> useCase.execute(uuid)
        );

        verify(gateway, never()).findById(uuid);
        verify(gateway, never()).save(any());
    }
}