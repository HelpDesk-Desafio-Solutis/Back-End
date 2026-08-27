package br.com.notification.microservice.core.consumer;

import br.com.notification.microservice.core.domain.NotificationDomain;
import br.com.notification.microservice.core.enums.Status;
import br.com.notification.microservice.core.enums.Type;
import br.com.notification.microservice.core.gateway.NotificationGateway;
import br.com.notification.microservice.infra.service.EMailService;
import br.com.shared.events.TicketAssignedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
public class TicketAssignedConsumer {

    private final NotificationGateway notificationGateway;
    private final EMailService emailService;

    @RabbitListener(
            queues = "notification.assigned.queue"
    )
    public void receive(TicketAssignedEvent event) {
        System.out.println("Ticket atribuído: " + event.ticketId());

        NotificationDomain notification = new NotificationDomain();
        notification.setEmail(event.email());
        notification.setClientUuid(event.clientId());
        notification.setTechnicianUuid(event.technicianId());
        notification.setTicketUuid(event.ticketId());
        notification.setMessage(event.message());

        notification.setStatus(Status.PENDING);
        notification.setType(Type.TICKET_ASSIGNED);

        notification.setCreatedAt(LocalDateTime.now());

        System.out.println(
                "ANTES DO SAVE CLIENT UUID: "
                        + notification.getClientUuid()
        );

        NotificationDomain saved = notificationGateway.save(notification);

        try {
            emailService.enviarEmailAtribuicao(event);
            saved.setStatus(Status.SENT);
        } catch(Exception e){
            saved.setStatus(Status.FAILED);
        }

        notificationGateway.save(saved);
    }

}