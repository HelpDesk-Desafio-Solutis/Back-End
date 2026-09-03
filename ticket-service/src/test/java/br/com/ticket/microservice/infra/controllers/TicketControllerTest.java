package br.com.ticket.microservice.infra.controllers;

import br.com.ticket.microservice.core.app.usecases.*;
import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.enums.Status;
import br.com.ticket.microservice.infra.controller.TicketController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
@WithMockUser(username = "user", roles = "CLIENT")
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private CreateTicketUseCase createTicketUseCase;

    @MockitoBean
    private CreateTicketAdminUseCase createTicketAdminUseCase;

    @MockitoBean
    private DeactivateTicketByIdUseCase deactivateTicketByIdUseCase;

    @MockitoBean
    private GetAllTicketUseCase getAllTicketUseCase;

    @MockitoBean
    private GetAllTicketByClientUseCase getAllTicketByClientUseCase;

    @MockitoBean
    private GetAllTicketByTechnicianUseCase getAllTicketByTechnicianUseCase;

    @MockitoBean
    private GetAllAvailableTicketsUseCase getAllAvailableTicketsUseCase;

    @MockitoBean
    private GetTicketByIdUseCase getTicketByIdUseCase;

    @MockitoBean
    private UpdateTicketByIdUseCase updateTicketByIdUseCase;


    @Test
    void shouldCreateTicket() throws Exception {

        UUID clientUuid = UUID.randomUUID();

        TicketDomain ticket = new TicketDomain();

        when(createTicketUseCase.execute(
                any(TicketDomain.class),
                eq("Bearer token"),
                eq(clientUuid)
        )).thenReturn(ticket);

        String json = """
                {
                    "title": "Problema no computador",
                    "description": "Computador não liga",
                    "category": "HARDWARE",
                    "priority": "HIGH"
                }
                """;

        mockMvc.perform(
                        post("/tickets")
                                .with(csrf())
                                .header("Authorization", "Bearer token")
                                .header("X-User-UUID", clientUuid)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated());

        verify(createTicketUseCase).execute(
                any(TicketDomain.class),
                eq("Bearer token"),
                eq(clientUuid)
        );
    }


    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldCreateTicketAsAdmin() throws Exception {

        String authorization = "Bearer token";

        UUID clientUuid = UUID.randomUUID();
        UUID technicianUuid = UUID.randomUUID();

        TicketDomain ticket = new TicketDomain();

        when(createTicketAdminUseCase.execute(
                any(TicketDomain.class),
                eq(authorization)
        )).thenReturn(ticket);

        mockMvc.perform(
                        post("/tickets/admin")
                                .with(csrf())
                                .header("Authorization", authorization)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                            "title": "Problema no servidor",
                            "description": "Servidor indisponível",
                            "category": "NETWORK",
                            "priority": "HIGH",
                            "clientUuid": "%s",
                            "technicianUuid": "%s"
                        }
                    """.formatted(clientUuid, technicianUuid))
                )
                .andExpect(status().isCreated());

        verify(createTicketAdminUseCase)
                .execute(
                        any(TicketDomain.class),
                        eq(authorization)
                );
    }


    @Test
    void shouldGetAllTickets() throws Exception {

        when(getAllTicketUseCase.execute(
                eq(Status.OPEN),
                eq("Bearer token")
        )).thenReturn(List.of());

        mockMvc.perform(
                        get("/tickets")
                                .param("status", "OPEN")
                                .header("Authorization", "Bearer token")
                )
                .andExpect(status().isOk());

        verify(getAllTicketUseCase)
                .execute(Status.OPEN, "Bearer token");
    }


    @Test
    void shouldGetAllTicketsWithoutStatus() throws Exception {

        when(getAllTicketUseCase.execute(
                isNull(),
                eq("Bearer token")
        )).thenReturn(List.of());

        mockMvc.perform(
                        get("/tickets")
                                .header("Authorization", "Bearer token")
                )
                .andExpect(status().isOk());

        verify(getAllTicketUseCase)
                .execute(null, "Bearer token");
    }


    @Test
    void shouldGetTicketById() throws Exception {

        UUID id = UUID.randomUUID();

        TicketDomain ticket = new TicketDomain();

        when(getTicketByIdUseCase.execute(
                id,
                "Bearer token"
        )).thenReturn(ticket);

        mockMvc.perform(
                        get("/tickets/{id}", id)
                                .header("Authorization", "Bearer token")
                )
                .andExpect(status().isOk());

        verify(getTicketByIdUseCase)
                .execute(id, "Bearer token");
    }


    @Test
    @WithMockUser(username = "tecnico", roles = "TECHNICIAN")
    void shouldUpdateTicket() throws Exception {

        UUID id = UUID.randomUUID();
        UUID technicianUuid = UUID.randomUUID();
        UUID userUuid = UUID.randomUUID();

        String authorization = "Bearer token";
        String userRole = "TECHNICIAN";

        TicketDomain ticket = new TicketDomain();

        when(updateTicketByIdUseCase.execute(
                any(TicketDomain.class),
                eq(id),
                eq(authorization),
                eq(userUuid),
                eq(userRole)
        )).thenReturn(ticket);

        String json = """
        {
            "title": "Título atualizado",
            "description": "Descrição atualizada",
            "technicianUuid": "%s"
        }
        """.formatted(technicianUuid);

        mockMvc.perform(
                        put("/tickets/{id}", id)
                                .with(csrf())
                                .header("Authorization", authorization)
                                .header("X-User-UUID", userUuid)
                                .header("X-User-Role", userRole)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk());

        verify(updateTicketByIdUseCase)
                .execute(
                        any(TicketDomain.class),
                        eq(id),
                        eq(authorization),
                        eq(userUuid),
                        eq(userRole)
                );
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldDeactivateTicket() throws Exception {

        UUID id = UUID.randomUUID();

        doNothing()
                .when(deactivateTicketByIdUseCase)
                .execute(id);

        mockMvc.perform(
                        delete("/tickets/{id}", id)
                                .with(csrf())
                                .header("Authorization", "Bearer token")
                                .header("X-User-Role", "ADMIN")
                )
                .andExpect(status().isNoContent());

        verify(deactivateTicketByIdUseCase)
                .execute(id);
    }


    @Test
    void shouldGetAllTicketsByClient() throws Exception {

        UUID clientId = UUID.randomUUID();

        when(getAllTicketByClientUseCase.execute(
                clientId,
                "Bearer token"
        )).thenReturn(List.of());

        mockMvc.perform(
                        get("/tickets/client/{id}", clientId)
                                .header("Authorization", "Bearer token")
                )
                .andExpect(status().isOk());

        verify(getAllTicketByClientUseCase)
                .execute(clientId, "Bearer token");
    }
}