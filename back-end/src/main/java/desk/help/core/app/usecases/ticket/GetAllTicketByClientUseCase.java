package desk.help.core.app.usecases.ticket;

import desk.help.core.domains.TicketDomain;
import desk.help.core.gateway.TicketGateway;

import java.util.List;
import java.util.UUID;

public class GetAllTicketByClientUseCase {

    private final TicketGateway gateway;

    public GetAllTicketByClientUseCase(TicketGateway gateway) {
        this.gateway = gateway;
    }

    public List<TicketDomain> execute(UUID uuid) {
        return gateway.findAllByClientId(uuid);
    }

}
