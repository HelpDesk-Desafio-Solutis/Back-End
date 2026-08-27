package br.com.ticket.microservice.core.gateway;

import br.com.ticket.microservice.core.domain.UserDomain;

import java.util.Optional;
import java.util.UUID;

public interface UserGateway {

    Optional<UserDomain> findById(UUID uuid);
}
