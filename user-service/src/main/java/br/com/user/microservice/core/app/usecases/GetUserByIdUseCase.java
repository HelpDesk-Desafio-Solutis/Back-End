package br.com.user.microservice.core.app.usecases;

import br.com.shared.exceptions.exceptionClass.EntityNotFoundException;
import br.com.user.microservice.core.domain.UserDomain;
import br.com.user.microservice.core.gateway.UserGateway;

import java.util.UUID;

public class GetUserByIdUseCase {

    private final UserGateway gateway;

    public GetUserByIdUseCase(UserGateway gateway) {
        this.gateway = gateway;
    }

    public UserDomain execute(UUID uuid) {
        return gateway.findByIdAndActiveTrue(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Id do usuário não encontrado"));
    }

}
