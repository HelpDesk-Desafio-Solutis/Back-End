package br.com.notification.microservice.core.consumer;

import br.com.notification.microservice.core.domain.NotificationDomain;
import br.com.notification.microservice.core.enums.Status;
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
        notification.setUserUuid(event.userId());
        notification.setEmail(event.email());
        notification.setMessage(event.message());
        notification.setCreatedAt(LocalDateTime.now());

        notification.setStatus(Status.PENDING);

        NotificationDomain savedNotification =
                notificationGateway.save(notification);

        System.out.println(
                "Notificação persistida como PENDING: "
                        + savedNotification.getUuid()
        );

        try {

            emailService.enviarEmailConfirmacao(event);

            savedNotification.setStatus(Status.SENT);

            notificationGateway.save(savedNotification);

            System.out.println(
                    "Notificação marcada como SENT: "
                            + savedNotification.getUuid()
            );

        } catch (Exception e) {

            savedNotification.setStatus(Status.FAILED);

            notificationGateway.save(savedNotification);

            System.err.println(
                    "Falha ao enviar notificação: "
                            + savedNotification.getUuid()
            );

            throw e;
        }
    }
}