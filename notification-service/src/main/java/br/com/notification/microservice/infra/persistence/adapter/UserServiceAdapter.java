package br.com.notification.microservice.infra.persistence.adapter;

import br.com.notification.microservice.core.app.dto.user.UserResponseDto;
import br.com.notification.microservice.core.gateway.UserGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class UserServiceAdapter implements UserGateway {

    private final RestTemplate restTemplate;

    @Value("${user-service.url}")
    private String userServiceUrl;


    public UserServiceAdapter() {
        this.restTemplate = new RestTemplate();
    }


    @Override
    public UserResponseDto findById(
            UUID uuid,
            String authorization
    ) {


        HttpHeaders headers = new HttpHeaders();

        headers.set(
                "Authorization",
                authorization
        );


        HttpEntity<Void> entity =
                new HttpEntity<>(headers);


        ResponseEntity<UserResponseDto> response =
                restTemplate.exchange(
                        userServiceUrl + "/users/" + uuid,
                        HttpMethod.GET,
                        entity,
                        UserResponseDto.class
                );


        return response.getBody();
    }
}