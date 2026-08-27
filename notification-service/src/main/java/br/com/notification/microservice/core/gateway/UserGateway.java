package br.com.notification.microservice.core.gateway;

import br.com.notification.microservice.core.app.dto.user.UserResponseDto;

import java.util.UUID;

public interface UserGateway {

    UserResponseDto findById(UUID uuid, String authorizationHeader);
}