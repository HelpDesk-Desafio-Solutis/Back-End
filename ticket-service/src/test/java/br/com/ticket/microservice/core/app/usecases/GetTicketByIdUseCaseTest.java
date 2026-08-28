package br.com.ticket.microservice.core.app.usecases;

import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.domain.UserDomain;
import br.com.ticket.microservice.core.gateway.TicketGateway;
import br.com.ticket.microservice.core.gateway.UserGateway;
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
class GetTicketByIdUseCaseTest {

    @Mock
    private TicketGateway gateway;

    @Mock
    private UserGateway userGateway;

    private GetTicketByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetTicketByIdUseCase(
                gateway,
                userGateway
        );
    }

    @Test
    void shouldReturnTicketWithClient() {

        UUID ticketId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();

        String authorization = "Bearer token";

        TicketDomain ticket = mock(TicketDomain.class);
        UserDomain client = mock(UserDomain.class);

        when(ticket.getClientDomain()).thenReturn(client);
        when(client.getUuid()).thenReturn(clientId);

        when(gateway.findById(ticketId))
                .thenReturn(Optional.of(ticket));

        when(userGateway.findById(clientId, authorization))
                .thenReturn(Optional.of(client));

        TicketDomain result =
                useCase.execute(ticketId, authorization);

        assertEquals(ticket, result);

        verify(ticket).setClientDomain(client);
        verify(ticket).setTechnicianDomain(null);
    }

    @Test
    void shouldThrowExceptionWhenTicketDoesNotExist() {

        UUID ticketId = UUID.randomUUID();

        when(gateway.findById(ticketId))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> useCase.execute(
                        ticketId,
                        "Bearer token"
                )
        );

        verifyNoInteractions(userGateway);
    }

    @Test
    void shouldThrowExceptionWhenClientDoesNotExist() {

        UUID ticketId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();

        String authorization = "Bearer token";

        TicketDomain ticket = mock(TicketDomain.class);
        UserDomain client = mock(UserDomain.class);

        when(ticket.getClientDomain()).thenReturn(client);
        when(client.getUuid()).thenReturn(clientId);

        when(gateway.findById(ticketId))
                .thenReturn(Optional.of(ticket));

        when(userGateway.findById(clientId, authorization))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> useCase.execute(ticketId, authorization)
        );
    }
}