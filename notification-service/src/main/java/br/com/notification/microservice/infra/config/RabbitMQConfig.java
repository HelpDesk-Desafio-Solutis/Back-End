package br.com.notification.microservice.infra.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "ticket.exchange";

    public static final String CREATED_QUEUE = "notification.queue";

    public static final String ASSIGNED_QUEUE = "notification.assigned.queue";

    public static final String CREATED_ROUTING_KEY = "ticket.created";

    public static final String ASSIGNED_ROUTING_KEY = "ticket.assigned";


    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue createdQueue() {
        return new Queue(CREATED_QUEUE);
    }


    @Bean
    public Binding createdBinding(
            Queue createdQueue,
            TopicExchange exchange
    ) {

        return BindingBuilder
                .bind(createdQueue)
                .to(exchange)
                .with(CREATED_ROUTING_KEY);
    }

    @Bean
    public Queue assignedQueue() {
        return new Queue(ASSIGNED_QUEUE);
    }


    @Bean
    public Binding assignedBinding(
            Queue assignedQueue,
            TopicExchange exchange
    ) {

        return BindingBuilder
                .bind(assignedQueue)
                .to(exchange)
                .with(ASSIGNED_ROUTING_KEY);
    }

}