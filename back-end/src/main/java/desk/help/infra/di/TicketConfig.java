package desk.help.infra.di;

import desk.help.core.app.usecases.ticket.*;
import desk.help.core.gateway.TicketGateway;
import desk.help.core.gateway.UserGateway;
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

}
