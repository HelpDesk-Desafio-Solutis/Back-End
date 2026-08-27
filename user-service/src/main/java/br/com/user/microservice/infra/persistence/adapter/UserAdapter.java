package br.com.user.microservice.infra.persistence.adapter;

import br.com.user.microservice.core.domain.UserDomain;
import br.com.user.microservice.core.enums.Role;
import br.com.user.microservice.core.gateway.UserGateway;
import br.com.user.microservice.infra.mapper.UserMapper;
import br.com.user.microservice.infra.persistence.repo.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAdapter implements UserGateway {

    private final JpaUserRepository repository;

    @Override
    public UserDomain save(UserDomain domain) {
        var entity = UserMapper.toJpaEntity(domain);
        var savedEntity = repository.save(entity);
        return UserMapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return repository.existsById(uuid);
    }

    @Override
    public List<UserDomain> findAll() {
        return repository.findAll().stream()
                .map(UserMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<UserDomain> findById(UUID uuid) {
        return repository.findById(uuid).map(UserMapper::toDomain);
    }

    @Override
    public UserDomain deactivateById(UUID uuid) {
        Optional<UserDomain> userOpt = findById(uuid);
            userOpt.ifPresent(a -> repository.deactivateByUuid(uuid));
            return userOpt.orElse(null);
    }

    @Override
    public boolean existsByEmailIgnoreCase(String email) {
        return repository.existsByEmailIgnoreCase(email);
    }

    @Override
    public boolean existsByIdNotAndEmailIgnoreCase(UUID uuid, String email) {
        return repository.existsByUuidNotAndEmailIgnoreCase(uuid, email);
    }

    @Override
    public boolean existsByIdAndActiveFalse(UUID uuid) {
        return repository.existsByUuidAndIsActiveFalse(uuid);
    }

    @Override
    public boolean existsByIdAndActiveTrue(UUID uuid) {
        return repository.existsByUuidAndIsActiveTrue(uuid);
    }

    @Override
    public boolean existsByIdNotAndRole(UUID uuid, Role role) {
        return repository.existsByUuidNotAndRole(uuid, role);
    }

    @Override
    public List<UserDomain> findAllByActiveTrue() {
        return repository.findAllByIsActiveTrue().stream()
                .map(UserMapper::toDomain)
                .toList();
    }

    @Override
    public List<UserDomain> findAllByActiveTrueAndRole(Role role) {
        return repository.findAllByIsActiveTrueAndRole(role).stream()
                .map(UserMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<UserDomain> findByIdAndActiveTrue(UUID uuid) {
        return repository.findByUuidAndIsActiveTrue(uuid).map(UserMapper::toDomain);
    }

    @Override
    public Optional<UserDomain> findByEmail(String email) {
        return repository.findByEmail(email).map(UserMapper::toDomain);
    }

    @Override
    public boolean existsByRole(Role role) {
        return repository.existsByRole(role);
    }       

    @Override
    public List<UserDomain> findAllByRole(Role role) {
        return repository.findAllByRole(role).stream()
                .map(UserMapper::toDomain)
                .toList();
    }

}
