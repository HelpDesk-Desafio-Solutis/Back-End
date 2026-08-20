package desk.help.core.app.usecases.user;

import desk.help.core.app.usecases.exceptions.ConflictException;
import desk.help.core.app.usecases.exceptions.exceptionClass.InactiveEntityException;
import desk.help.core.domains.UserDomain;
import desk.help.core.gateway.UserGateway;
import jakarta.persistence.EntityNotFoundException;

import java.util.UUID;

public class UpdateUserByIdUseCase {

    private final UserGateway gateway;

    public UpdateUserByIdUseCase(UserGateway gateway) {
        this.gateway = gateway;
    }

    public UserDomain execute(UserDomain user, UUID uuid) {
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

        if (gateway.existsByIdNotAndEmailIgnoreCase(uuid, user.getEmail())) {
            throw new ConflictException(
                    //TODO: Adicionar audit action aqui
                    "placeholder"
            );
        }

        user.setUuid(uuid);
        user.setCreatedAt(gateway.findById(uuid).get().getCreatedAt());
        user.setUpdatedAt(java.time.LocalDateTime.now());
        return gateway.save(user);
    }
}
