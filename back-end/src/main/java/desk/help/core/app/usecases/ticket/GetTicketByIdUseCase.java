package desk.help.core.app.usecases.ticket;

import desk.help.core.domains.TicketDomain;
import desk.help.core.gateway.TicketGateway;

import java.util.UUID;

public class GetTicketByIdUseCase {

    private final TicketGateway gateway;

    public GetTicketByIdUseCase(TicketGateway gateway) {
        this.gateway = gateway;
    }

    public TicketDomain execute(UUID uuid) {
        return gateway.findById(uuid).get();
    }

}
