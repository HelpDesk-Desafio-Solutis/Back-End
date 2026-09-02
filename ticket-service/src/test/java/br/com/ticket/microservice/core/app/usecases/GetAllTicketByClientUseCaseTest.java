package br.com.ticket.microservice.core.app.usecases;

import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.gateway.TicketGateway;
import br.com.ticket.microservice.core.gateway.UserGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllTicketByClientUseCaseTest {

    @Mock
    private TicketGateway gateway;

    @Mock
    private UserGateway userGateway;

    private GetAllTicketByClientUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetAllTicketByClientUseCase(gateway, userGateway);
    }

    @Test
    void shouldReturnTicketsByClient() {

        UUID clientId = UUID.randomUUID();

        List<TicketDomain> tickets = List.of(
                mock(TicketDomain.class),
                mock(TicketDomain.class)
        );

        when(gateway.findAllByClientId(clientId))
                .thenReturn(tickets);

        List<TicketDomain> result = useCase.execute(clientId, null);

        assertEquals(tickets, result);

        verify(gateway).findAllByClientId(clientId);
    }
}