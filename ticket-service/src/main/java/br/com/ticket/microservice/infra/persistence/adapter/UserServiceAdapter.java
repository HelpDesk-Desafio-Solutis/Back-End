package br.com.ticket.microservice.infra.persistence.adapter;

import br.com.ticket.microservice.core.app.dto.user.UserResponseDto;
import br.com.ticket.microservice.core.domain.UserDomain;
import br.com.ticket.microservice.core.gateway.UserGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceAdapter implements UserGateway {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${user-service.base-url}")
    private String userServiceBaseUrl;

    @Override
    public Optional<UserDomain> findById(UUID uuid) {
        try {
            ResponseEntity<UserResponseDto> response = restTemplate.exchange(
                    userServiceBaseUrl + "/users/{id}",
                    HttpMethod.GET,
                    new HttpEntity<>(buildHeaders()),
                    UserResponseDto.class,
                    uuid
            );

            UserResponseDto body = response.getBody();
            if (body == null) {
                return Optional.empty();
            }

            UserDomain user = new UserDomain();
            user.setUuid(body.getUuid());
            user.setName(body.getName());
            user.setEmail(body.getEmail());
            user.setRole(body.getRole());
            return Optional.of(user);
        } catch (HttpClientErrorException.NotFound ex) {
            return Optional.empty();
        }
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes servletAttributes) {
            HttpServletRequest request = servletAttributes.getRequest();
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorization != null && !authorization.isBlank()) {
                headers.set(HttpHeaders.AUTHORIZATION, authorization);
            }
        }
        return headers;
    }
}
