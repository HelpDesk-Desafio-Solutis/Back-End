package br.com.ticket.microservice.core.app.usecases;

import br.com.shared.exceptions.exceptionClass.RelatedEntityNotFoundException;
import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.domain.UserDomain;
import br.com.ticket.microservice.core.gateway.NotificationGateway;
import br.com.ticket.microservice.core.gateway.TicketGateway;
import br.com.ticket.microservice.core.gateway.UserGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTicketUseCaseTest {

    @Mock
    private TicketGateway ticketGateway;

    @Mock
    private UserGateway userGateway;

    @Mock
    private NotificationGateway notificationGateway;

    @InjectMocks
    private CreateTicketUseCase useCase;

    private UUID clientUuid;
    private UUID technicianUuid;

    private UserDomain client;
    private UserDomain technician;
    private TicketDomain ticket;

    @BeforeEach
    void setUp() {

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

        ticket = new TicketDomain();



    }

    private void authenticateUser() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldCreateTicketSuccessfully() throws Exception {

        authenticateUser();

        String authorization = "Bearer token";

        ticket.setTechnicianDomain(technician);

        when(userGateway.findById(clientUuid, authorization))
                .thenReturn(Optional.of(client));

        when(userGateway.findById(technicianUuid, authorization))
                .thenReturn(Optional.of(technician));

        when(ticketGateway.save(ticket))
                .thenReturn(ticket);

        TicketDomain result =
                useCase.execute(
                        ticket,
                        authorization,
                        clientUuid
                );

        assertNotNull(result);

        assertEquals(client, result.getClientDomain());
        assertEquals(technician, result.getTechnicianDomain());

        assertNull(result.getUuid());

        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        verify(userGateway)
                .findById(clientUuid, authorization);

        verify(userGateway)
                .findById(technicianUuid, authorization);

        verify(ticketGateway)
                .save(ticket);

        verify(notificationGateway)
                .sendTicketCreated(ticket);

        verify(notificationGateway)
                .sendTicketAssigned(ticket);
    }

    @Test
    void shouldCreateTicketWithoutTechnician() throws Exception {
        authenticateUser();

        String authorization = "Bearer token";

        ticket.setTechnicianDomain(null);

        when(userGateway.findById(clientUuid, authorization))
                .thenReturn(Optional.of(client));

        when(ticketGateway.save(ticket))
                .thenReturn(ticket);

        TicketDomain result =
                useCase.execute(
                        ticket,
                        authorization,
                        clientUuid
                );

        assertNotNull(result);

        assertEquals(client, result.getClientDomain());
        assertNull(result.getTechnicianDomain());

        verify(userGateway)
                .findById(clientUuid, authorization);

        verify(ticketGateway)
                .save(ticket);

        verify(notificationGateway)
                .sendTicketCreated(ticket);

        verify(notificationGateway, never())
                .sendTicketAssigned(any());
    }

    @Test
    void shouldThrowExceptionWhenAuthenticationIsMissing() {

        SecurityContextHolder.clearContext();

        String authorization = "Bearer token";

        assertThrows(
                RelatedEntityNotFoundException.class,
                () -> useCase.execute(
                        ticket,
                        authorization,
                        clientUuid
                )
        );

        verifyNoInteractions(userGateway);
        verifyNoInteractions(ticketGateway);
        verifyNoInteractions(notificationGateway);
    }

    @Test
    void shouldThrowExceptionWhenUserUuidIsNull() {

        String authorization = "Bearer token";

        assertThrows(
                RelatedEntityNotFoundException.class,
                () -> useCase.execute(
                        ticket,
                        authorization,
                        null
                )
        );

        verifyNoInteractions(userGateway);
        verifyNoInteractions(ticketGateway);
        verifyNoInteractions(notificationGateway);
    }

    @Test
    void shouldThrowExceptionWhenClientDoesNotExist() {

        authenticateUser();

        String authorization = "Bearer token";

        when(userGateway.findById(clientUuid, authorization))
                .thenReturn(Optional.empty());

        assertThrows(
                RelatedEntityNotFoundException.class,
                () -> useCase.execute(
                        ticket,
                        authorization,
                        clientUuid
                )
        );

        verify(userGateway)
                .findById(clientUuid, authorization);

        verifyNoInteractions(ticketGateway);
        verifyNoInteractions(notificationGateway);
    }

    @Test
    void shouldThrowExceptionWhenTechnicianDoesNotExist() {

        authenticateUser();

        String authorization = "Bearer token";

        ticket.setTechnicianDomain(technician);

        when(userGateway.findById(clientUuid, authorization))
                .thenReturn(Optional.of(client));

        when(userGateway.findById(technicianUuid, authorization))
                .thenReturn(Optional.empty());

        assertThrows(
                RelatedEntityNotFoundException.class,
                () -> useCase.execute(
                        ticket,
                        authorization,
                        clientUuid
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
}