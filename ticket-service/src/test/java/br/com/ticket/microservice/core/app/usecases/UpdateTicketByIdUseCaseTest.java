package br.com.ticket.microservice.core.app.usecases;

import br.com.shared.exceptions.exceptionClass.RelatedEntityNotFoundException;
import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.domain.UserDomain;
import br.com.ticket.microservice.core.enums.Status;
import br.com.ticket.microservice.core.gateway.NotificationGateway;
import br.com.ticket.microservice.core.gateway.TicketGateway;
import br.com.ticket.microservice.core.gateway.UserGateway;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
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
class UpdateTicketByIdUseCaseTest {

    @Mock
    private TicketGateway ticketGateway;

    @Mock
    private UserGateway userGateway;

    @Mock
    private NotificationGateway notificationGateway;

    @InjectMocks
    private UpdateTicketByIdUseCase useCase;

    private UUID ticketUuid;
    private UUID clientUuid;
    private UUID technicianUuid;

    private TicketDomain existingTicket;
    private TicketDomain updatedTicket;

    private UserDomain client;
    private UserDomain technician;

    @BeforeEach
    void setUp() {

        ticketUuid = UUID.randomUUID();
        clientUuid = UUID.randomUUID();
        technicianUuid = UUID.randomUUID();

        client = new UserDomain();
        client.setUuid(clientUuid);
        client.setName("João");
        client.setEmail("joao@email.com");

        technician = new UserDomain();
        technician.setUuid(technicianUuid);
        technician.setName("Técnico");
        technician.setEmail("tecnico@email.com");

        existingTicket = new TicketDomain();
        existingTicket.setUuid(ticketUuid);
        existingTicket.setClientDomain(client);
        existingTicket.setTechnicianDomain(technician);
        existingTicket.setTitle("Título antigo");
        existingTicket.setDescription("Descrição antiga");
        existingTicket.setStatus(Status.OPEN);

        updatedTicket = new TicketDomain();
        updatedTicket.setTitle("Título atualizado");
        updatedTicket.setDescription("Descrição atualizada");
    }

    @Test
    void shouldUpdateTicketSuccessfully() {

        String authorization = "Bearer token";

        when(ticketGateway.findById(ticketUuid))
                .thenReturn(Optional.of(existingTicket));

        when(userGateway.findById(clientUuid, authorization))
                .thenReturn(Optional.of(client));

        when(userGateway.findById(technicianUuid, authorization))
                .thenReturn(Optional.of(technician));

        when(ticketGateway.save(existingTicket))
                .thenReturn(existingTicket);

        TicketDomain result =
                useCase.execute(
                        updatedTicket,
                        ticketUuid,
                        authorization
                );

        assertNotNull(result);

        assertEquals(ticketUuid, result.getUuid());

        assertEquals(
                "Título atualizado",
                result.getTitle()
        );

        assertEquals(
                "Descrição atualizada",
                result.getDescription()
        );

        assertEquals(client, result.getClientDomain());
        assertEquals(technician, result.getTechnicianDomain());

        verify(ticketGateway)
                .findById(ticketUuid);

        verify(userGateway)
                .findById(clientUuid, authorization);

        verify(userGateway)
                .findById(technicianUuid, authorization);

        verify(ticketGateway)
                .save(existingTicket);

        verifyNoInteractions(notificationGateway);
    }

    @Test
    void shouldThrowExceptionWhenTicketDoesNotExist() {

        String authorization = "Bearer token";

        when(ticketGateway.findById(ticketUuid))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(
                        updatedTicket,
                        ticketUuid,
                        authorization
                )
        );

        verify(ticketGateway)
                .findById(ticketUuid);

        verifyNoInteractions(userGateway);
        verifyNoInteractions(notificationGateway);
    }

    @Test
    void shouldThrowExceptionWhenClientDoesNotExist() {

        String authorization = "Bearer token";

        when(ticketGateway.findById(ticketUuid))
                .thenReturn(Optional.of(existingTicket));

        when(userGateway.findById(clientUuid, authorization))
                .thenReturn(Optional.empty());

        assertThrows(
                RelatedEntityNotFoundException.class,
                () -> useCase.execute(
                        updatedTicket,
                        ticketUuid,
                        authorization
                )
        );

        verify(ticketGateway)
                .findById(ticketUuid);

        verify(userGateway)
                .findById(clientUuid, authorization);

        verify(userGateway, never())
                .findById(technicianUuid, authorization);

        verify(ticketGateway, never())
                .save(any());

        verifyNoInteractions(notificationGateway);
    }

    @Test
    void shouldThrowExceptionWhenTechnicianDoesNotExist() {

        String authorization = "Bearer token";

        when(ticketGateway.findById(ticketUuid))
                .thenReturn(Optional.of(existingTicket));

        when(userGateway.findById(clientUuid, authorization))
                .thenReturn(Optional.of(client));

        when(userGateway.findById(technicianUuid, authorization))
                .thenReturn(Optional.empty());

        assertThrows(
                RelatedEntityNotFoundException.class,
                () -> useCase.execute(
                        updatedTicket,
                        ticketUuid,
                        authorization
                )
        );

        verify(userGateway)
                .findById(clientUuid, authorization);

        verify(userGateway)
                .findById(technicianUuid, authorization);

        verify(ticketGateway, never())
                .save(any());

        verifyNoInteractions(notificationGateway);
    }

    @Test
    void shouldSendNotificationWhenStatusChanges() {

        String authorization = "Bearer token";

        when(ticketGateway.findById(ticketUuid))
                .thenReturn(Optional.of(existingTicket));

        when(userGateway.findById(clientUuid, authorization))
                .thenReturn(Optional.of(client));

        when(userGateway.findById(technicianUuid, authorization))
                .thenReturn(Optional.of(technician));

        updatedTicket.setStatus(Status.CLOSED);

        when(ticketGateway.save(existingTicket))
                .thenReturn(existingTicket);

        useCase.execute(
                updatedTicket,
                ticketUuid,
                authorization
        );

        verify(notificationGateway)
                .sendTicketStatusChanged(
                        existingTicket,
                        Status.OPEN
                );
    }
}