package br.com.user.microservice.infra.persistence.repo;

import br.com.user.microservice.core.enums.Role;
import br.com.user.microservice.infra.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<UserJpaEntity, UUID> {

    boolean existsByEmailIgnoreCase(String email);
    boolean existsByUuidNotAndEmailIgnoreCase(UUID uuid, String email);
    boolean existsByUuidAndIsActiveFalse(UUID uuid);
    boolean existsByUuidAndIsActiveTrue(UUID uuid);
    @Modifying
    @Query("UPDATE UserJpaEntity u SET u.isActive = false WHERE u.uuid = :uuid")
    void deactivateByUuid(@Param("uuid") UUID uuid);
    List<UserJpaEntity> findAllByIsActiveTrue();
    List<UserJpaEntity> findAllByIsActiveTrueAndRole(Role role);
    Optional<UserJpaEntity> findByUuidAndIsActiveTrue(UUID uuid);
    Optional<UserJpaEntity> findByEmail(String email);
    boolean existsByRole(Role role);
    boolean existsByUuidNotAndRole(UUID uuid, Role role);
    List<UserJpaEntity> findAllByRole(Role role);

}
