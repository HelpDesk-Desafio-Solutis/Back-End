package desk.help.core.app.usecases.user;

import desk.help.core.app.usecases.exceptions.ConflictException;
import desk.help.core.domains.UserDomain;
import desk.help.core.enums.Role;
import desk.help.core.gateway.PasswordEncoderGateway;
import desk.help.core.gateway.UserGateway;

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