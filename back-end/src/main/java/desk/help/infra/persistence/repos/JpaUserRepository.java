package desk.help.infra.persistence.repos;

import desk.help.core.enums.Role;
import desk.help.infra.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<UserJpaEntity, UUID> {

    boolean existsByEmailIgnoreCase(String email);
    boolean existsByIdNotAndEmailIgnoreCase(UUID uuid, String email);
    boolean existsByIdAndIsActiveFalse(UUID uuid);
    boolean existsByIdAndIsActiveTrue(UUID uuid);
    UserJpaEntity deactivateById(UUID uuid);
    List<UserJpaEntity> findAllByIsActiveTrue();
    Optional<UserJpaEntity> findByIdAndIsActiveTrue(UUID uuid);
    Optional<UserJpaEntity> findByEmail(String email);
    boolean existsByRole(Role role);
    boolean existsByIdNotAndRole(UUID uuid, Role role);
    List<UserJpaEntity> findAllByRole(Role role);

}
