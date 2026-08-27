package br.com.notification.microservice.infra.service;

import br.com.shared.events.TicketAssignedEvent;
import br.com.shared.events.TicketCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EMailService {

    @Autowired
    private JavaMailSender sender;

    public void enviarEmailConfirmacao(TicketCreatedEvent event) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(event.email());
        message.setSubject("Confirmação de Ticket");

        message.setText(
                "Olá,\n\n" +
                        "Seu ticket foi criado com sucesso.\n\n" +
                        "Mensagem: " + event.message() + "\n\n" +
                        "Aguarde o atendimento do técnico responsável.\n\n" +
                        "Atenciosamente,\n" +
                        "Equipe de Suporte"
        );

        sender.send(message);

        System.out.println(
                "📤 E-mail de confirmação enviado para: "
                        + event.email()
        );
    }

    public void enviarEmailAtribuicao(TicketAssignedEvent event) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(event.email());
        message.setSubject("Atribuicao de Ticket");

        message.setText(
                "Olá,\n\n" +
                        "Você foi atribuido a um ticket.\n\n" +
                        "Começe a trabalhar no ticket o mais rápido possível.\n\n" +
                        "Atenciosamente,\n" +
                        "Equipe de Suporte"
        );

        sender.send(message);

        System.out.println(
                "📤 E-mail de atribuição enviado para: "
                        + event.email()
        );
    }

}