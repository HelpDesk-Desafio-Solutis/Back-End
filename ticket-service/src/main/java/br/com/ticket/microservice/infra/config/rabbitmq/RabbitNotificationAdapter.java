package br.com.ticket.microservice.infra.config.rabbitmq;

import br.com.shared.events.TicketAssignedEvent;
import br.com.shared.events.TicketCreatedEvent;
import br.com.shared.events.TicketStatusChangedEvent;
import br.com.ticket.microservice.core.enums.Status;
import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.gateway.NotificationGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitNotificationAdapter implements NotificationGateway {

    private final RabbitTemplate template;

    @Override
    public void sendTicketCreated(TicketDomain domain) {
        if (domain.getClientDomain() == null) {
            throw new IllegalArgumentException("Não é possível enviar notificação de ticket criado sem o cliente associado.");
        }

        TicketCreatedEvent event = new TicketCreatedEvent(
                domain.getUuid(),
                domain.getClientDomain().getUuid(),
                domain.getTechnicianDomain() != null ? domain.getTechnicianDomain().getUuid() : null,
                domain.getClientDomain().getEmail(),
                "Ticket criado com sucesso"
        );
            template.convertAndSend("ticket.exchange", "ticket.created", event);
    }

    @Override
    public void sendTicketAssigned(TicketDomain domain) {
        if(domain.getClientDomain() == null){
            throw new IllegalArgumentException(
                    "Ticket sem cliente associado."
            );
        }

        TicketAssignedEvent event = new TicketAssignedEvent(
                        domain.getUuid(),
                        domain.getClientDomain().getUuid(),
                        domain.getTechnicianDomain().getUuid(),
                        domain.getTechnicianDomain().getEmail(),
                        "Você foi atribuido a um ticket."
                );

        template.convertAndSend("ticket.exchange", "ticket.assigned", event);
    }

    @Override
    public void sendTicketStatusChanged(TicketDomain domain, Status oldStatus) {
        if (domain.getClientDomain() == null) {
            throw new IllegalArgumentException(
                    "Ticket sem cliente associado."
            );
        }

        TicketStatusChangedEvent event = new TicketStatusChangedEvent(
                        domain.getUuid(),
                        domain.getClientDomain().getUuid(),
                        domain.getTechnicianDomain() != null ? domain.getTechnicianDomain().getUuid() : null,
                        domain.getClientDomain().getEmail(),
                        oldStatus.name(),
                        domain.getStatus().name(),
                        "O status do ticket foi alterado de " + oldStatus.name() + " para " + domain.getStatus().name()
                );

        template.convertAndSend("ticket.exchange", "ticket.status.changed", event);

    }

}
