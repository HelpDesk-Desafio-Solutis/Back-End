package br.com.user.microservice.core.gateway;

import br.com.user.microservice.core.domain.UserDomain;
import br.com.user.microservice.core.enums.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserGateway {

    UserDomain save(UserDomain userDomain);
    boolean existsById(UUID uuid);
    List<UserDomain> findAll();
    Optional<UserDomain> findById(UUID uuid);
    UserDomain deactivateById(UUID uuid);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByIdNotAndEmailIgnoreCase(UUID uuid, String email);
    boolean existsByIdAndActiveFalse(UUID uuid);
    boolean existsByIdAndActiveTrue(UUID uuid);
    boolean existsByIdNotAndRole(UUID uuid, Role role);
    List<UserDomain> findAllByActiveTrue();
    List<UserDomain> findAllByActiveTrueAndRole(Role role);
    Optional<UserDomain> findByIdAndActiveTrue(UUID uuid);
    Optional<UserDomain> findByEmail(String email);


    boolean existsByRole(Role role);

    List<UserDomain> findAllByRole(Role role);
}
