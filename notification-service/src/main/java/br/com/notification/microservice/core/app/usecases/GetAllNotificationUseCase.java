package br.com.notification.microservice.core.app.usecases;

import br.com.notification.microservice.core.app.dto.NotificationResponseDto;
import br.com.notification.microservice.core.app.dto.user.UserResponseDto;
import br.com.notification.microservice.core.gateway.NotificationGateway;
import br.com.notification.microservice.core.gateway.UserGateway;

import java.util.List;

public class GetAllNotificationUseCase {

    private final NotificationGateway notificationGateway;
    private final UserGateway userGateway;

    public GetAllNotificationUseCase(
            NotificationGateway notificationGateway,
            UserGateway userGateway
    ) {
        this.notificationGateway = notificationGateway;
        this.userGateway = userGateway;
    }


    public List<NotificationResponseDto> execute(String authorizationHeader) {

        return notificationGateway.findAll()
                .stream()
                .map(notification -> {

                    UserResponseDto user =
                            userGateway.findById(
                                    notification.getClientUuid(),
                                    authorizationHeader
                            );

                    return new NotificationResponseDto(
                            user.getName(),
                            user.getEmail(),
                            notification.getMessage(),
                            notification.getStatus().name()
                    );

                })
                .toList();
    }
}