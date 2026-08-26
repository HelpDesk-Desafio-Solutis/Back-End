package br.com.notification.microservice.core.consumer;

import br.com.shared.events.TicketCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TicketCreatedConsumer {

    @RabbitListener(
            queues = "notification.queue"
    )
    public void receive(TicketCreatedEvent event) {
        System.out.println("Novo ticket criado: " + event.ticketId());
        sendNotification(event);
    }


    private void sendNotification(TicketCreatedEvent event) {
        System.out.println("Enviando E-Mail para: " + event.email());
    }

}
