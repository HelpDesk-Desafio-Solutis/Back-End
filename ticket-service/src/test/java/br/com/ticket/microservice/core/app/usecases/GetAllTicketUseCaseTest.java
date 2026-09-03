package br.com.ticket.microservice.core.app.usecases;

import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.domain.UserDomain;
import br.com.ticket.microservice.core.enums.Status;
import br.com.ticket.microservice.core.gateway.TicketGateway;
import br.com.ticket.microservice.core.gateway.UserGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllTicketUseCaseTest {

    @Mock
    private TicketGateway gateway;

    @Mock
    private UserGateway userGateway;

    private GetAllTicketUseCase useCase;

    private String authorization;

    @BeforeEach
    void setUp() {
        useCase = new GetAllTicketUseCase(gateway, userGateway);
        authorization = "Bearer token";
    }

    @Test
    void shouldReturnAllTickets() {

        TicketDomain ticket1 = mock(TicketDomain.class);
        TicketDomain ticket2 = mock(TicketDomain.class);

        List<TicketDomain> tickets = List.of(
                ticket1,
                ticket2
        );

        when(gateway.findAll())
                .thenReturn(tickets);

        List<TicketDomain> result =
                useCase.execute(null, authorization);

        assertEquals(tickets, result);

        verify(gateway)
                .findAll();
    }

    @Test
    void shouldReturnTicketsByStatus() {

        Status status = Status.OPEN;

        TicketDomain ticket = mock(TicketDomain.class);

        List<TicketDomain> tickets = List.of(ticket);

        when(gateway.findAll(status))
                .thenReturn(tickets);

        List<TicketDomain> result =
                useCase.execute(status, authorization);

        assertEquals(tickets, result);

        verify(gateway)
                .findAll(status);
    }

    @Test
    void shouldEnrichClientAndTechnician() {

        UUID clientUuid = UUID.randomUUID();
        UUID technicianUuid = UUID.randomUUID();

        UserDomain client = new UserDomain();
        client.setUuid(clientUuid);
        client.setName("João");
        client.setEmail("joao@email.com");

        UserDomain technician = new UserDomain();
        technician.setUuid(technicianUuid);
        technician.setName("Técnico");
        technician.setEmail("tecnico@email.com");

        TicketDomain ticket = new TicketDomain();

        UserDomain clientReference = new UserDomain();
        clientReference.setUuid(clientUuid);

        UserDomain technicianReference = new UserDomain();
        technicianReference.setUuid(technicianUuid);

        ticket.setClientDomain(clientReference);
        ticket.setTechnicianDomain(technicianReference);

        when(gateway.findAll())
                .thenReturn(List.of(ticket));

        when(userGateway.findById(clientUuid, authorization))
                .thenReturn(Optional.of(client));

        when(userGateway.findById(technicianUuid, authorization))
                .thenReturn(Optional.of(technician));

        List<TicketDomain> result =
                useCase.execute(null, authorization);

        assertEquals(client, result.get(0).getClientDomain());
        assertEquals(technician, result.get(0).getTechnicianDomain());

        verify(userGateway)
                .findById(clientUuid, authorization);

        verify(userGateway)
                .findById(technicianUuid, authorization);
    }
}