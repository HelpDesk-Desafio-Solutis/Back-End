package br.com.notification.microservice.core.app.usecases;

import br.com.notification.microservice.core.app.dto.NotificationResponseDto;
import br.com.notification.microservice.core.app.dto.user.UserResponseDto;
import br.com.notification.microservice.core.domain.NotificationDomain;
import br.com.notification.microservice.core.gateway.NotificationGateway;
import br.com.notification.microservice.core.gateway.UserGateway;

import java.util.Optional;
import java.util.UUID;

public class GetNotificationByIdUseCase {

    private final NotificationGateway notificationGateway;
    private final UserGateway userGateway;

    public GetNotificationByIdUseCase(
            NotificationGateway notificationGateway,
            UserGateway userGateway
    ) {
        this.notificationGateway = notificationGateway;
        this.userGateway = userGateway;
    }

    public Optional<NotificationResponseDto> execute(UUID uuid, String authorizationHeader) {

        Optional<NotificationDomain> notificationOptional =
                notificationGateway.findById(uuid);

        if (notificationOptional.isEmpty()) {
            return Optional.empty();
        }

        NotificationDomain notification = notificationOptional.get();

        UserResponseDto user =
                userGateway.findById(notification.getUserUuid(), authorizationHeader);

        NotificationResponseDto response =
                new NotificationResponseDto(
                        user.getName(),
                        user.getEmail(),
                        notification.getMessage(),
                        notification.getStatus().name()
                );

        return Optional.of(response);
    }
}