package br.com.user.microservice.core.app.usecases;

import br.com.user.microservice.core.domain.UserDomain;
import br.com.user.microservice.core.gateway.UserGateway;

import java.util.List;

public class GetAllUsersUseCase {

    private final UserGateway gateway;

    public GetAllUsersUseCase(UserGateway gateway) {
        this.gateway = gateway;
    }

    public List<UserDomain> execute() {
        List<UserDomain> users = gateway.findAllByActiveTrue();
        return users;
    }

}
