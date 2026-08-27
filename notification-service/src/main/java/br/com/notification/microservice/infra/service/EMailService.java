package br.com.notification.microservice.infra.service;

import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.domain.UserDomain;
import br.com.ticket.microservice.infra.persistence.repo.JpaTicketRepository;
import br.com.user.microservice.infra.persistence.repo.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EMailService {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaTicketRepository ticketRepository;

    private final WebClient userWebClient;
    private final WebClient ticketWebClient;

    @Autowired
    public EMailService(WebClient.Builder webClientBuilder) {
        this.userWebClient = webClientBuilder.baseUrl("http://localhost:8081").build();
        this.ticketWebClient = webClientBuilder.baseUrl("http://localhost:8082").build();
    }

    public void enviarEmailConfirmacao(TicketDomain entity) {
        SimpleMailMessage message = new SimpleMailMessage();

        String urlUser = "/users/" + entity.getClientDomain().getUuid();
        UserDomain user = userWebClient.get()
                .uri(urlUser)
                .retrieve()
                .bodyToMono(UserDomain.class)
                .block();

        String urlTicket = "/tickets/" + entity.getUuid();
        TicketDomain ticket = ticketWebClient.get()
                .uri(urlTicket)
                .retrieve()
                .bodyToMono(TicketDomain.class)
                .block();

        message.setTo(user.getEmail());
        message.setSubject("Confirmação de Ticket");
        message.setText("Olá " + user.getName() + ",\n\nSeu ticket com o título '" + ticket.getTitle() + "' foi criado com sucesso.\n\n" + "Aguarde o atendimento do técnico responsável.\n\nAtenciosamente,\nEquipe de Suporte");
        sender.send(message);

        System.out.println("📤 E-mail de confirmação enviado para: " + user.getEmail());
    }

}
