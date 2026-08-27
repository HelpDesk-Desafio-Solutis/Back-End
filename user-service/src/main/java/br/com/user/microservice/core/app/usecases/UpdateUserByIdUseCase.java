package br.com.user.microservice.core.app.usecases;

import br.com.shared.exceptions.ConflictException;
import br.com.shared.exceptions.exceptionClass.InactiveEntityException;
import br.com.user.microservice.core.domain.UserDomain;
import br.com.user.microservice.core.gateway.UserGateway;
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
