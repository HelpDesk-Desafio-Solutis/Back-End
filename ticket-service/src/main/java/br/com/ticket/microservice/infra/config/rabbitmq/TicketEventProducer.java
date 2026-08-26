package br.com.ticket.microservice.infra.config.rabbitmq;

import br.com.shared.events.TicketCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketEventProducer {

    private final RabbitTemplate template;

    public void send(TicketCreatedEvent event) {
        template.convertAndSend("ticket.exchange", "ticket.created", event);
    }

}
