package br.com.ticket.microservice.infra.config.rabbitmq;

import br.com.shared.events.TicketCreatedEvent;
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
                domain.getClientDomain().getEmail(),
                "Ticket criado com sucesso"
        );
            template.convertAndSend("ticket.exchange", "ticket.created", event);
    }
}
