package desk.help.core.app.usecases.user;

import desk.help.core.domains.UserDomain;
import desk.help.core.gateway.UserGateway;

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
