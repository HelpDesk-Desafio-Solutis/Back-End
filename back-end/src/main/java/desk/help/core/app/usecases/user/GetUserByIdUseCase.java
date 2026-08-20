package desk.help.core.app.usecases.user;

import desk.help.core.app.usecases.exceptions.exceptionClass.EntityNotFoundException;
import desk.help.core.domains.UserDomain;
import desk.help.core.gateway.UserGateway;

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
