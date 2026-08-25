package br.com.user.microservice.core.app.usecases;

import br.com.shared.exceptions.ConflictException;
import br.com.shared.gateway.PasswordEncoderGateway;
import br.com.user.microservice.core.domain.UserDomain;
import br.com.user.microservice.core.enums.Role;
import br.com.user.microservice.core.gateway.UserGateway;

import java.time.LocalDateTime;

public class CreateUserUseCase {

    private final UserGateway gateway;
    private final PasswordEncoderGateway encoderGateway;

    public CreateUserUseCase(
            UserGateway userGateway,
            PasswordEncoderGateway encoderGateway
    ) {
        this.gateway = userGateway;
        this.encoderGateway = encoderGateway;
    }

    public UserDomain execute(UserDomain user) {

        if (gateway.existsByEmailIgnoreCase(user.getEmail())) {
            throw new ConflictException("E-mail já cadastrado");
        }

        user.setUuid(null);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        user.setRole(
                user.getRole() != null
                        ? user.getRole()
                        : Role.CLIENT
        );

        user.setPassword(
                encoderGateway.encode(user.getPassword())
        );

        return gateway.save(user);
    }
}