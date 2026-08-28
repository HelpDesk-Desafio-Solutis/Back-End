package br.com.ticket.microservice.core.app.usecases;

import br.com.shared.exceptions.exceptionClass.RelatedEntityNotFoundException;
import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.domain.UserDomain;
import br.com.ticket.microservice.core.gateway.NotificationGateway;
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
class CreateTicketAdminUseCaseTest {

    @Mock
    private TicketGateway ticketGateway;

    @Mock
    private UserGateway userGateway;

    @Mock
    private NotificationGateway notificationGateway;

    private CreateTicketAdminUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateTicketAdminUseCase(
                ticketGateway,
                userGateway,
                notificationGateway
        );
    }

    @Test
    void shouldCreateTicketAsAdmin() {

        UUID clientId = UUID.randomUUID();
        UUID technicianId = UUID.randomUUID();

        String authorization = "Bearer token";

        TicketDomain ticket = mock(TicketDomain.class);

        UserDomain client = mock(UserDomain.class);
        UserDomain technician = mock(UserDomain.class);

        when(ticket.getClientDomain()).thenReturn(client);
        when(ticket.getTechnicianDomain()).thenReturn(technician);

        when(client.getUuid()).thenReturn(clientId);
        when(technician.getUuid()).thenReturn(technicianId);

        when(userGateway.findById(clientId, authorization))
                .thenReturn(Optional.of(client));

        when(userGateway.findById(technicianId, authorization))
                .thenReturn(Optional.of(technician));

        when(ticketGateway.save(ticket))
                .thenReturn(ticket);

        TicketDomain result =
                useCase.execute(ticket, authorization);

        assertNotNull(result);

        verify(userGateway)
                .findById(clientId, authorization);

        verify(userGateway)
                .findById(technicianId, authorization);

        verify(ticketGateway)
                .save(ticket);

        verify(notificationGateway)
                .sendTicketCreated(ticket);

        verify(notificationGateway)
                .sendTicketAssigned(ticket);
    }

    @Test
    void shouldThrowExceptionWhenClientDoesNotExist() {

        UUID clientId = UUID.randomUUID();

        String authorization = "Bearer token";

        TicketDomain ticket = mock(TicketDomain.class);

        UserDomain client = mock(UserDomain.class);

        when(ticket.getClientDomain()).thenReturn(client);
        when(client.getUuid()).thenReturn(clientId);

        when(userGateway.findById(clientId, authorization))
                .thenReturn(Optional.empty());

        assertThrows(
                RelatedEntityNotFoundException.class,
                () -> useCase.execute(ticket, authorization)
        );

        verify(userGateway)
                .findById(clientId, authorization);

        verify(ticketGateway, never()).save(any());

        verifyNoInteractions(notificationGateway);
    }

    @Test
    void shouldThrowExceptionWhenTechnicianDoesNotExist() {

        UUID clientId = UUID.randomUUID();
        UUID technicianId = UUID.randomUUID();

        String authorization = "Bearer token";

        TicketDomain ticket = mock(TicketDomain.class);

        UserDomain client = mock(UserDomain.class);
        UserDomain technician = mock(UserDomain.class);

        when(ticket.getClientDomain()).thenReturn(client);
        when(ticket.getTechnicianDomain()).thenReturn(technician);

        when(client.getUuid()).thenReturn(clientId);
        when(technician.getUuid()).thenReturn(technicianId);

        when(userGateway.findById(clientId, authorization))
                .thenReturn(Optional.of(client));

        when(userGateway.findById(technicianId, authorization))
                .thenReturn(Optional.empty());

        assertThrows(
                RelatedEntityNotFoundException.class,
                () -> useCase.execute(ticket, authorization)
        );

        verify(ticketGateway, never()).save(any());
        verifyNoInteractions(notificationGateway);
    }
}