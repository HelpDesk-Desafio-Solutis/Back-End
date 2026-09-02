package br.com.ticket.microservice.infra.di;

import br.com.ticket.microservice.core.app.usecases.*;
import br.com.ticket.microservice.core.gateway.NotificationGateway;
import br.com.ticket.microservice.core.gateway.TicketGateway;
import br.com.ticket.microservice.core.gateway.UserGateway;
import br.com.ticket.microservice.infra.persistence.adapter.UserServiceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TicketConfig {

    @Bean
    public CreateTicketUseCase createTicketUseCase(TicketGateway ticketGateway, UserGateway userGateway, NotificationGateway notificationGateway) {
        return new CreateTicketUseCase(ticketGateway, userGateway, notificationGateway);
    }

    @Bean
    public CreateTicketAdminUseCase createTicketAdminUseCase(TicketGateway ticketGateway, UserGateway userGateway, NotificationGateway notificationGateway) {
        return new CreateTicketAdminUseCase(ticketGateway, userGateway, notificationGateway);
    }

    @Bean
    public DeactivateTicketByIdUseCase deactivateTicketByIdUseCase(TicketGateway ticketGateway) {
        return new DeactivateTicketByIdUseCase(ticketGateway);
    }

    @Bean
    public GetAllTicketUseCase getAllTicketUseCase(TicketGateway ticketGateway, UserGateway userGateway) {
        return new GetAllTicketUseCase(ticketGateway, userGateway);
    }

    @Bean
    public GetAllTicketByClientUseCase getAllTicketByClientUseCase(TicketGateway ticketGateway, UserGateway userGateway) {
        return new GetAllTicketByClientUseCase(ticketGateway, userGateway);
    }

    @Bean
    public GetAllTicketByTechnicianUseCase getAllTicketByTechnicianUseCase(TicketGateway ticketGateway, UserGateway userGateway) {
        return new GetAllTicketByTechnicianUseCase(ticketGateway, userGateway);
    }

    @Bean
    public UpdateTicketByIdUseCase updateTicketByIdUseCase(TicketGateway ticketGateway, UserGateway userGateway, NotificationGateway notificationGateway) {
        return new UpdateTicketByIdUseCase(ticketGateway, userGateway, notificationGateway);
    }

    @Bean
    public GetTicketByIdUseCase getTicketByIdUseCase(TicketGateway ticketGateway, UserGateway userGateway) {
        return new GetTicketByIdUseCase(ticketGateway, userGateway);
    }

    @Bean
    public GetAllAvailableTicketsUseCase getAllAvailableTicketsUseCase(TicketGateway ticketGateway, UserGateway userGateway) {
        return new GetAllAvailableTicketsUseCase(ticketGateway, userGateway);
    }

    @Bean
    public UserGateway userGateway(UserServiceAdapter userServiceAdapter) {
        return userServiceAdapter;
    }

}
