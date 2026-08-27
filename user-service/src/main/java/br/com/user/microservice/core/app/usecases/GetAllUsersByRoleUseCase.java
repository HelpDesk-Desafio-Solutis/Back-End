package br.com.user.microservice.core.app.usecases;

import br.com.user.microservice.core.domain.UserDomain;
import br.com.user.microservice.core.enums.Role;
import br.com.user.microservice.core.gateway.UserGateway;

import java.util.List;

public class GetAllUsersByRoleUseCase {

    private final UserGateway gateway;

    public GetAllUsersByRoleUseCase(UserGateway gateway) {
        this.gateway = gateway;
    }

    public List<UserDomain> execute(Role role) {
        List<UserDomain> users = gateway.findAllByActiveTrueAndRole(role);
        return users;
    }

}
