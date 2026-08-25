package desk.help.core.app.usecases.ticket;

import desk.help.core.domains.TicketDomain;
import desk.help.core.enums.Status;
import desk.help.core.gateway.TicketGateway;

import java.util.List;

public class GetAllTicketUseCase {

    private final TicketGateway gateway;

    public GetAllTicketUseCase(TicketGateway gateway) {
        this.gateway = gateway;
    }

    public List<TicketDomain> execute() {
        return gateway.findAll();
    }

    public List<TicketDomain> execute(Status status) {
        return gateway.findAll(status);
    }

}
