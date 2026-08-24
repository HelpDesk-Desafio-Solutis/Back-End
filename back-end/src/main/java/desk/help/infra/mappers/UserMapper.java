package desk.help.infra.mappers;

import desk.help.core.app.dto.login.UserTokenDto;
import desk.help.core.app.dto.user.UserRequestDto;
import desk.help.core.app.dto.user.UserResponseDto;
import desk.help.core.domains.UserDomain;
import desk.help.infra.persistence.entity.UserJpaEntity;

import java.util.UUID;

public class UserMapper {

    /* ========= TOKEN DTO ========= */

    public static UserTokenDto of(UserDomain user, String token) {
        if (user == null) return null;

        UserTokenDto dto = new UserTokenDto();

        dto.setUserId(user.getUuid());
        dto.setEmail(user.getEmail());
        dto.setNome(user.getName());
        dto.setToken(token);

        // Usa a role real do usuário
        dto.setTipoUsuario(
                user.getRole() != null
                        ? user.getRole().name()
                        : null
        );

        return dto;
    }


    /* ========= DTO -> DOMAIN ========= */

    public static UserDomain toDomain(UserRequestDto req) {
        if (req == null) return null;

        UserDomain user = new UserDomain();

        user.setName(req.getName());
        user.setEmail(req.getEmail());

        // Senha para cadastro/login
        user.setPassword(req.getPassword());

        user.setRole(req.getRole());

        return user;
    }


    /* ========= UUID -> DOMAIN ========= */

    public static UserDomain toDomain(UUID id) {
        if (id == null) return null;

        UserDomain user = new UserDomain();
        user.setUuid(id);

        return user;
    }


    /* ========= DOMAIN -> DTO ========= */

    public static UserResponseDto toResponseDto(UserDomain user) {
        if (user == null) return null;

        return UserResponseDto.builder()
                .uuid(user.getUuid())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }


    /* ========= DOMAIN -> JPA ========= */

    public static UserJpaEntity toJpaEntity(UserDomain user) {
        if (user == null) return null;

        return UserJpaEntity.builder()
                .uuid(user.getUuid())
                .name(user.getName())
                .email(user.getEmail())

                // Senha armazenada no banco
                .password(user.getPassword())

                .isActive(
                        user.getActive() != null
                                ? user.getActive()
                                : true
                )

                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }


    /* ========= DOMAIN -> JPA (simples) ========= */

    public static UserJpaEntity toJpaEntitySimple(UserDomain user) {
        if (user == null) return null;

        return UserJpaEntity.builder()
                .uuid(user.getUuid())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }


    /* ========= JPA -> DOMAIN ========= */

    public static UserDomain toDomain(UserJpaEntity entity) {
        if (entity == null) return null;

        UserDomain user = new UserDomain();

        user.setUuid(entity.getUuid());
        user.setName(entity.getName());
        user.setEmail(entity.getEmail());

        // Senha necessária para autenticação
        user.setPassword(entity.getPassword());

        user.setActive(entity.getIsActive());
        user.setRole(entity.getRole());
        user.setCreatedAt(entity.getCreatedAt());
        user.setUpdatedAt(entity.getUpdatedAt());

        return user;
    }
}