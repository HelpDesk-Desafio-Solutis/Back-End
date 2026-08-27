package br.com.notification.microservice.core.consumer;

import br.com.notification.microservice.core.domain.NotificationDomain;
import br.com.notification.microservice.core.enums.Status;
import br.com.notification.microservice.core.enums.Type;
import br.com.notification.microservice.core.gateway.NotificationGateway;
import br.com.notification.microservice.infra.service.EMailService;
import br.com.shared.events.TicketCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TicketCreatedConsumer {

    private final NotificationGateway notificationGateway;
    private final EMailService emailService;

    @RabbitListener(queues = "notification.queue")
    public void receive(TicketCreatedEvent event) {

        System.out.println(
                "Novo ticket criado: " + event.ticketId()
        );

        NotificationDomain notification = new NotificationDomain();

        notification.setTicketUuid(event.ticketId());
        notification.setClientUuid(event.clientId());
        notification.setTechnicianUuid(event.technicianUuid());
        notification.setEmail(event.email());
        notification.setMessage(event.message());
        notification.setCreatedAt(LocalDateTime.now());

        notification.setStatus(Status.PENDING);
        notification.setType(Type.TICKET_CREATED);

        NotificationDomain savedNotification = notificationGateway.save(notification);
        try {
            emailService.enviarEmailConfirmacao(event);
            savedNotification.setStatus(Status.SENT);
            notificationGateway.save(savedNotification);
        } catch (Exception e) {
            savedNotification.setStatus(Status.FAILED);
            notificationGateway.save(savedNotification);
            throw e;
        }
    }
}