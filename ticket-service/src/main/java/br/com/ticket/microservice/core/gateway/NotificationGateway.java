package br.com.ticket.microservice.core.gateway;

import br.com.ticket.microservice.core.domain.TicketDomain;

public interface NotificationGateway {

    void sendTicketCreated(TicketDomain domain);

    void sendTicketAssigned(TicketDomain domain);

}
