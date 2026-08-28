package br.com.ticket.microservice.core.app.usecases;

import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.enums.Status;
import br.com.ticket.microservice.core.gateway.TicketGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllTicketUseCaseTest {

    @Mock
    private TicketGateway gateway;

    private GetAllTicketUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetAllTicketUseCase(gateway);
    }

    @Test
    void shouldReturnAllTickets() {

        List<TicketDomain> tickets = List.of(
                mock(TicketDomain.class),
                mock(TicketDomain.class)
        );

        when(gateway.findAll()).thenReturn(tickets);

        List<TicketDomain> result = useCase.execute();

        assertEquals(tickets, result);

        verify(gateway).findAll();
    }

    @Test
    void shouldReturnTicketsByStatus() {

        Status status = Status.OPEN;

        List<TicketDomain> tickets = List.of(
                mock(TicketDomain.class)
        );

        when(gateway.findAll(status)).thenReturn(tickets);

        List<TicketDomain> result = useCase.execute(status);

        assertEquals(tickets, result);

        verify(gateway).findAll(status);
    }
}