package desk.help.infra.di;

import desk.help.core.app.usecases.user.*;
import desk.help.core.gateway.PasswordEncoderGateway;
import desk.help.core.gateway.UserGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(UserGateway userGateway, PasswordEncoderGateway encoderGateway) {
        return new CreateUserUseCase(userGateway, encoderGateway);
    }

    @Bean
    public GetAllUsersUseCase getAllUsersUseCase(UserGateway gateway) {
        return new GetAllUsersUseCase(gateway);
    }

    @Bean
    public GetAllUsersByRoleUseCase getAllUsersByRoleUseCase(UserGateway gateway) {
        return new GetAllUsersByRoleUseCase(gateway);
    }

    @Bean
    public GetUserByIdUseCase getUserByIdUseCase(UserGateway gateway) {
        return new GetUserByIdUseCase(gateway);
    }

    @Bean
    public UpdateUserByIdUseCase updateUserByIdUseCase(UserGateway gateway) {
        return new UpdateUserByIdUseCase(gateway);
    }

    @Bean
    public DeactivateUserUseCase deactivateUserUseCase(UserGateway gateway) {
        return new DeactivateUserUseCase(gateway);
    }

}
