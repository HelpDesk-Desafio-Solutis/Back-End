package br.com.ticket.microservice.core.gateway;

import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.enums.Status;

public interface NotificationGateway {

    void sendTicketCreated(TicketDomain domain);

    void sendTicketAssigned(TicketDomain domain);

    void sendTicketStatusChanged(TicketDomain domain, Status oldStatus);

}
