package desk.help.core.app.usecases.user;

import desk.help.core.app.usecases.exceptions.exceptionClass.InactiveEntityException;
import desk.help.core.domains.UserDomain;
import desk.help.core.gateway.UserGateway;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.UUID;

public class DeactivateUserUseCase {

    private final UserGateway gateway;

    public DeactivateUserUseCase(UserGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(UUID uuid) {
        if (!gateway.existsById(uuid)) {
            throw new EntityNotFoundException(
                    //TODO: Adicionar audit action aqui
                    "placeholder"
            );
        }

        if (gateway.existsByIdAndActiveFalse(uuid)) {
            throw new InactiveEntityException(
                    //TODO: Adicionar audit action aqui
                    "placeholder"
            );
        }

        //TODO: Adicionar case de chamado que não esteja fechado.

        UserDomain user = gateway.findById(uuid).get();
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        gateway.save(user);
    }
}
