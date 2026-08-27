package br.com.notification.microservice.infra.config;

import br.com.notification.microservice.core.app.usecases.GetAllNotificationUseCase;
import br.com.notification.microservice.core.app.usecases.GetNotificationByIdUseCase;
import br.com.notification.microservice.core.gateway.NotificationGateway;
import br.com.notification.microservice.core.gateway.UserGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig {

    @Bean
    public GetAllNotificationUseCase getAllNotificationUseCase(NotificationGateway notificationGateway, UserGateway userGateway) {
        return new GetAllNotificationUseCase(notificationGateway, userGateway);
    }

    @Bean
    public GetNotificationByIdUseCase getNotificationByIdUseCase(NotificationGateway notificationGateway, UserGateway userGateway) {
        return new GetNotificationByIdUseCase(notificationGateway, userGateway);
    }

}
