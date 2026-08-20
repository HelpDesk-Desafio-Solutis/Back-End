package desk.help.core.app.usecases.user;

import desk.help.core.domains.UserDomain;
import desk.help.core.enums.Role;
import desk.help.core.gateway.UserGateway;

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
