package br.com.user.microservice.core.app.usecases;

import br.com.shared.exceptions.ConflictException;
import br.com.shared.exceptions.exceptionClass.InactiveEntityException;
import br.com.shared.gateway.PasswordEncoderGateway;
import br.com.user.microservice.core.domain.UserDomain;
import br.com.user.microservice.core.gateway.UserGateway;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.UUID;

public class UpdateUserByIdUseCase {

    private final UserGateway gateway;
    private final PasswordEncoderGateway encoderGateway;

    public UpdateUserByIdUseCase(UserGateway gateway, PasswordEncoderGateway encoderGateway) {
        this.gateway = gateway;
        this.encoderGateway = encoderGateway;
    }

    public UserDomain execute(UserDomain user, UUID uuid) {

        if (!gateway.existsById(uuid)) {
            throw new EntityNotFoundException("Usuário de ID %s não encontrado".formatted(uuid));
        }

        if (gateway.existsByIdAndActiveFalse(uuid)) {
            throw new InactiveEntityException("Usuário de ID %s está inativo".formatted(uuid));
        }

        if (gateway.existsByIdNotAndEmailIgnoreCase(uuid, user.getEmail())) {
            throw new ConflictException("O e-mail %s já está cadastrado".formatted(user.getEmail()));
        }

        UserDomain existingUser = gateway.findById(uuid).get();

        user.setUuid(uuid);
        user.setCreatedAt(existingUser.getCreatedAt());
        user.setUpdatedAt(LocalDateTime.now());
        user.setPassword(encoderGateway.encode(user.getPassword()));

        return gateway.save(user);
    }
}