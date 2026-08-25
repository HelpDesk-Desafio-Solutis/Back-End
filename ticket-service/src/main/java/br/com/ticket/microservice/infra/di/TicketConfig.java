package br.com.ticket.microservice.infra.di;

import br.com.ticket.microservice.core.app.usecases.*;
import br.com.ticket.microservice.core.gateway.TicketGateway;
import br.com.user.microservice.core.gateway.UserGateway;
import br.com.user.microservice.infra.persistence.adapter.UserAdapter;
import br.com.user.microservice.infra.persistence.repo.JpaUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TicketConfig {

    @Bean
    public CreateTicketUseCase createTicketUseCase(TicketGateway ticketGateway, UserGateway userGateway) {
        return new CreateTicketUseCase(ticketGateway, userGateway);
    }

    @Bean
    public DeactivateTicketByIdUseCase deactivateTicketByIdUseCase(TicketGateway ticketGateway) {
        return new DeactivateTicketByIdUseCase(ticketGateway);
    }

    @Bean
    public GetAllTicketUseCase getAllTicketUseCase(TicketGateway ticketGateway) {
        return new GetAllTicketUseCase(ticketGateway);
    }

    @Bean
    public GetAllTicketByClientUseCase getAllTicketByClientUseCase(TicketGateway ticketGateway) {
        return new GetAllTicketByClientUseCase(ticketGateway);
    }

    @Bean
    public UpdateTicketByIdUseCase updateTicketByIdUseCase(TicketGateway ticketGateway, UserGateway userGateway) {
        return new UpdateTicketByIdUseCase(ticketGateway, userGateway);
    }

    @Bean
    public GetTicketByIdUseCase getTicketByIdUseCase(TicketGateway ticketGateway) {
        return new GetTicketByIdUseCase(ticketGateway);
    }

    @Bean
    public UserGateway userGateway(JpaUserRepository userRepository) {
        return new UserAdapter(userRepository);
    }

}
