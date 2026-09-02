package br.com.user.microservice.core.app.usecases;

import br.com.shared.exceptions.exceptionClass.InactiveEntityException;
import br.com.user.microservice.core.domain.UserDomain;
import br.com.user.microservice.core.gateway.UserGateway;
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
                    "Usuário com ID %s não encontrado".formatted(uuid)
            );
        }

        if (gateway.existsByIdAndActiveFalse(uuid)) {
            throw new InactiveEntityException(
                    "Usuário com ID %s já está inativo".formatted(uuid)
            );
        }

        //TODO: Adicionar case de chamado que não esteja fechado.

        UserDomain user = gateway.findById(uuid).get();
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        gateway.save(user);
    }
}
