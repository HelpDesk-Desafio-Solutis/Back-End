package br.com.notification.microservice.core.consumer;

import br.com.shared.events.TicketCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class TicketCreatedConsumer {

        private static final Logger log = LoggerFactory.getLogger(TicketCreatedConsumer.class);

    @RabbitListener(
            queues = "notification.queue"
    )
    public void receive(TicketCreatedEvent event) {
        log.info("Novo ticket criado: {}", event.ticketId());
        sendNotification(event);
    }


    private void sendNotification(TicketCreatedEvent event) {
        log.info("Enviando E-Mail para: {}", event.email());
    }

}
