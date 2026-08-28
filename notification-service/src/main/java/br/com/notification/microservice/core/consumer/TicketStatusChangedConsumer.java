package br.com.notification.microservice.core.consumer;

import br.com.notification.microservice.core.domain.NotificationDomain;
import br.com.notification.microservice.core.enums.Status;
import br.com.notification.microservice.core.enums.Type;
import br.com.notification.microservice.core.gateway.NotificationGateway;
import br.com.notification.microservice.infra.service.EMailService;
import br.com.shared.events.TicketStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
public class TicketStatusChangedConsumer {

    private final NotificationGateway notificationGateway;
    private final EMailService emailService;


    @RabbitListener(
            queues = "notification.status.changed.queue"
    )
    public void receive(TicketStatusChangedEvent event) {
        System.out.println("Status alterado do ticket: " + event.ticketId());

        NotificationDomain notification = new NotificationDomain();

        notification.setTicketUuid(event.ticketId());
        notification.setClientUuid(event.clientId());
        notification.setTechnicianUuid(event.technicianId());
        notification.setEmail(event.email());
        notification.setMessage(event.message());

        notification.setStatus(Status.PENDING);
        notification.setType(Type.TICKET_STATUS_CHANGED);

        notification.setCreatedAt(LocalDateTime.now());

        System.out.println("OLD STATUS: " + event.oldStatus());
        System.out.println("NEW STATUS: " + event.newStatus());

        NotificationDomain saved = notificationGateway.save(notification);

        try {
            emailService.enviarEmailStatusChanged(event);
            saved.setStatus(Status.SENT);
        } catch(Exception e) {
            e.printStackTrace();
            saved.setStatus(Status.FAILED);
        }

        notificationGateway.save(saved);
    }
}